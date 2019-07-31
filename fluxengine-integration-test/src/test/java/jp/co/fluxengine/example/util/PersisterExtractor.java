package jp.co.fluxengine.example.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import jp.co.fluxengine.example.CloudSqlPool;
import jp.co.fluxengine.stateengine.model.datom.Event;
import jp.co.fluxengine.stateengine.util.JacksonUtils;
import jp.co.fluxengine.stateengine.util.Serializer.KryoSerializer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class PersisterExtractor {

    private static final Logger LOG = LogManager.getLogger(PersisterExtractor.class);

    protected String projectId;

    protected String topic;

    protected ClassLoader remoteTestRunnerLoader;

    protected Class<?> remoteRunnerClass;

    protected Method publishOneTime;

    protected PersisterExtractor() throws ClassNotFoundException, NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        projectId = System.getenv("PROJECT");

        remoteTestRunnerLoader = getClassLoaderFromLib("fluxengine-remote-test-runner-.+\\.jar");
        remoteRunnerClass = remoteTestRunnerLoader.loadClass("jp.co.fluxengine.remote.test.RemoteRunner");
        remoteRunnerClass.getDeclaredMethod("setProjectId", String.class).invoke(null, projectId);

        publishOneTime = remoteRunnerClass.getDeclaredMethod("publishOneTime", String.class, String.class);

        Properties envProps = loadProperties("/publisher.properties");
        topic = envProps.getProperty("totopic");

        LOG.debug("topic = {}", topic);
    }

    public String getProjectId() {
        return projectId;
    }

    public abstract Map<String, Object> getAllAsMap() throws Exception;

    public abstract Map<String, Object> getEntriesAsMap(String[] keys) throws Exception;

    @SuppressWarnings("unchecked")
    public Map<String, Object> getIdMap(String id) throws Exception {
        return (Map<String, Object>) getEntriesAsMap(new String[]{id}).get(id);
    }

    public double currentPacketUsage(String userId, String name) throws Exception {
        String todayString = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());

        Map<String, Object> targetMap = getIdMap("[" + userId + "]");
        Map<String, Object> nameMap = getPersisterMap(targetMap, name);
        String lifetime = getNested(nameMap, String.class, "lifetime");

        return lifetime != null && lifetime.equals(todayString) ?
                getNested(nameMap, Number.class, "value", "使用量").doubleValue() :
                0.0;
    }

    public abstract Map<String, Object> getPersisterMap(Map<String, Object> idMap, String persisterName);

    public void publishOneTime(String inputJsonString) throws InvocationTargetException, IllegalAccessException {
        publishOneTime.invoke(null, inputJsonString, topic);
    }

    public static PersisterExtractor getInstance(String persisterDb) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, InstantiationException {
        switch (persisterDb) {
            case "datastore":
                return new DatastoreExtractor();
            case "memorystore":
                return new MemorystoreExtractor();
            default:
                throw new IllegalArgumentException("persisterDb must be either 'datastore' or 'memorystore'");
        }
    }

    public static PersisterExtractor getInstance() throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Properties fluxengineProps = loadProperties("/fluxengine.properties");
        return getInstance(fluxengineProps.getProperty("persister.db"));
    }

    public static ClassLoader getClassLoaderFromLib(String jarNameRegex) throws MalformedURLException {
        File jarFile = Arrays.stream(new File("lib").listFiles())
                .filter(file -> file.getName().matches(jarNameRegex))
                .findAny()
                .orElseThrow(() -> new RuntimeException(jarNameRegex + " にマッチするjarが見つかりませんでした"));
        return new URLClassLoader(new URL[]{jarFile.toURI().toURL()});
    }

    public static Properties loadProperties(String resourcePath) throws IOException {
        try (InputStream in = PersisterExtractor.class.getResourceAsStream(resourcePath)) {
            Properties props = new Properties();
            props.load(in);
            return props;
        }
    }

    public static <T> T getNested(Map<String, Object> map, Class<T> resultClazz, String... paths) {
        Object result = map;

        for (String path : paths) {
            if (result == null) {
                return null;
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> next = (Map<String, Object>) result;
            result = next.get(path);
        }

        return resultClazz.cast(result);
    }
}

class DatastoreExtractor extends PersisterExtractor {

    private static final Logger LOG = LogManager.getLogger(DatastoreExtractor.class);

    private String persisterNamespace;
    private String persisterKind;
    private Class<?> cloudStoreSelecterClass;

    protected DatastoreExtractor() throws ClassNotFoundException, NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        super();

        cloudStoreSelecterClass = remoteTestRunnerLoader.loadClass("jp.co.fluxengine.remote.test.CloudStoreSelecter");

        Properties persisterProps = loadProperties("/persisterDataStore.properties");
        persisterNamespace = persisterProps.getProperty("namespace");
        persisterKind = persisterProps.getProperty("kind");

        LOG.debug("namespace = {}, kind = {}", persisterNamespace, persisterKind);
    }

    @Override
    public Map<String, Object> getAllAsMap() throws Exception {
        File resultFile = File.createTempFile("persister", ".txt");

        // CloudStoreSelecterはexportEntityDataToFileを呼ぶたびにインスタンス内に結果を蓄積する
        // このテストでは毎回現在の状態のみが欲しいので
        // 呼ばれるごとにインスタンスを作成する
        Object cloudStoreSelecter = cloudStoreSelecterClass.getConstructor(String.class, String.class).newInstance(persisterNamespace, persisterKind);
        cloudStoreSelecterClass.getDeclaredMethod("exportEntityDataToFile", String.class).invoke(cloudStoreSelecter, resultFile.getAbsolutePath());

        String resultJsonString =
                "[" + FileUtils
                        .readFileToString(new File(resultFile.getAbsolutePath()), Charset.defaultCharset())
                        .trim()
                        .replace('\n', ',') + "]";
        LOG.debug("result = " + resultJsonString);

        resultFile.delete();

        List<Map<String, Object>> mapList = new ObjectMapper().readValue(resultJsonString, new TypeReference<List<Map<String, Object>>>() {
        });

        Map<String, Object> result = new HashMap<>();
        mapList.forEach(jsonMap -> {
            Map<String, Object> valueMap = new HashMap<>();
            if (jsonMap.containsKey("lifetime")) {
                valueMap.put("lifetime", jsonMap.get("lifetime"));
            }
            if (jsonMap.containsKey("value")) {
                valueMap.put("value", jsonMap.get("value"));
            }
            result.put((String) jsonMap.get("id"), valueMap);
        });

        return result;
    }

    @Override
    public Map<String, Object> getEntriesAsMap(String[] keys) throws Exception {
        // Datastoreの方は、キーを絞って取得する機能はないので、全件取得する
        return getAllAsMap();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPersisterMap(Map<String, Object> idMap, String persisterName) {
        return getNested(idMap, Map.class, "value", persisterName);
    }
}

class MemorystoreExtractor extends PersisterExtractor {

    private static final Logger LOG = LogManager.getLogger(MemorystoreExtractor.class);

    protected MemorystoreExtractor() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        super();
    }

    @Override
    public Map<String, Object> getAllAsMap() throws Exception {
        return getEntriesAsMap(new String[]{});
    }

    @Override
    public Map<String, Object> getEntriesAsMap(String[] keys) throws Exception {
        // DSLのプラグインによって、MemorystoreからCloud SQLに値を移す
        String requestId = UUID.randomUUID().toString();

        LOG.debug("requestId = {}, keys = {}", requestId, Arrays.toString(keys));

        Event inputEvent = new Event();
        inputEvent.setNamespace("memorystore/Memorystoreの内容取得");
        inputEvent.setEventName("Memorystore取得イベント");
        inputEvent.setCreateTime(LocalDateTime.now());
        Map<String, Object> propertyMap = Maps.newHashMap();
        propertyMap.put("requestid", requestId);
        propertyMap.put("keys", (keys == null || keys.length == 0) ?
                Lists.newArrayList() :
                Arrays.asList(keys));
        inputEvent.setProperty(propertyMap);
        List<Event> eventList = Lists.newArrayList();
        eventList.add(inputEvent);

        String inputJsonString = JacksonUtils.writeValueAsString(eventList);
        // Streamジョブが動いている前提で、イベントを発行する
        publishOneTime(inputJsonString);

        // すぐに処理されないはずなので、少し待つ
        Thread.sleep(5000);

        // Memorystoreのデータが取得されると、SQLにデータが格納される
        // たとえ0件であっても、データがないことを示すレコードが格納されるので、
        // レコードが取得できるまでループで待てばよい

        Map<String, Object> result = Maps.newHashMap();

        // ジョブが終了するとSQLにデータが入っているので、取得する
        try (Connection conn = CloudSqlPool.getDataSource().getConnection()) {
            PreparedStatement selectStmt = conn.prepareStatement("SELECT `key`, `value` FROM `memorystore_contents` WHERE `requestid` = ?");
            selectStmt.setString(1, requestId);

            while (true) {
                try (ResultSet rs = selectStmt.executeQuery()) {
                    // レコードがある場合、データを取得して終了する
                    // ない場合はまだデータ取得が完了していないので、待つ
                    if (rs.next()) {
                        String initKey = rs.getString(1);
                        LOG.debug("レコードあり: requestid = {}, initKey = {}", requestId, initKey);
                        // 0件の場合は、keyが空文字列のレコードが1件だけ格納されている
                        // その場合は何もせずに終了する
                        // 空文字列でない場合は、内容を取得する
                        if (!initKey.isEmpty()) {
                            try (InputStream in = rs.getBinaryStream(2)) {
                                byte[] value = IOUtils.toByteArray(in);
                                KryoSerializer serializer = new KryoSerializer(HashMap.class);
                                Map<String, Object> valueMap = serializer.deserialize(value);

                                result.put(initKey, valueMap);
                            }
                            while (rs.next()) {
                                String key = rs.getString(1);
                                try (InputStream in = rs.getBinaryStream(2)) {
                                    byte[] value = IOUtils.toByteArray(in);
                                    KryoSerializer serializer = new KryoSerializer(HashMap.class);
                                    Map<String, Object> valueMap = serializer.deserialize(value);

                                    result.put(key, valueMap);
                                }
                            }
                        }
                        break;
                    } else {
                        LOG.debug("レコード無し: requestid = {}", requestId);
                        Thread.sleep(3000);
                    }
                }
            }

            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM `memorystore_contents` WHERE `requestid` = ?");
            deleteStmt.setString(1, requestId);
            deleteStmt.execute();
        }

        LOG.debug("Memorystore = " + result.toString());

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPersisterMap(Map<String, Object> idMap, String persisterName) {
        return getNested(idMap, Map.class, persisterName);
    }
}