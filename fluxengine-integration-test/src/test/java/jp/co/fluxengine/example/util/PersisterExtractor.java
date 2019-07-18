package jp.co.fluxengine.example.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
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
import java.time.LocalDate;
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

    public Class<?> getRemoteRunnerClass() {
        return remoteRunnerClass;
    }

    public String getTopic() {
        return topic;
    }

    public String getProjectId() {
        return projectId;
    }

    public abstract Map<String, Object> getResultJson() throws Exception;

    public Map<String, Object> getResultJson(String id) throws Exception {
        return (Map<String, Object>) getResultJson().get(id);
    }

    public double currentPacketUsage(String userId, String name) throws Exception {
        String todayString = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());

        Map<String, Object> targetMap = getResultJson("[" + userId + "]");

        return targetMap.get("lifetime").equals(todayString) ?
                getNested(targetMap, double.class, "value", name, "value", "使用量") :
                0.0;
    }

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
            Map<String, Object> next = (Map<String, Object>) result;
            result = next.get(path);
        }

        return (T) result;
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
    public Map<String, Object> getResultJson() throws Exception {
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
        mapList.stream().forEach(jsonMap -> {
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

}

class MemorystoreExtractor extends PersisterExtractor {

    private Object memoryStoreSelecter;
    private Method selectAllData;

    protected MemorystoreExtractor() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, InstantiationException {
        super();

        Properties fluxengineProps = loadProperties("/fluxengine.properties");
        String host = fluxengineProps.getProperty("persister.memorystore.host");
        int port = Integer.parseInt(fluxengineProps.getProperty("persister.memorystore.port"));

        Class<?> memoryStoreSelecterClass = remoteTestRunnerLoader.loadClass("jp.co.fluxengine.remote.test.MemoryStoreSelecter");
        memoryStoreSelecter = memoryStoreSelecterClass.getConstructor(String.class, int.class).newInstance(host, port);
        selectAllData = memoryStoreSelecterClass.getDeclaredMethod("selectAllData");
    }

    @Override
    public Map<String, Object> getResultJson() throws Exception {
        return (Map<String, Object>) selectAllData.invoke(memoryStoreSelecter);
    }

}