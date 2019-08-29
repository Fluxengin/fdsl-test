package jp.co.fluxengine.example.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jp.co.fluxengine.example.util.Utils.getNested;

public abstract class PersisterExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(PersisterExtractor.class);

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

    public abstract IdToEntityMap getAll() throws Exception;

    public abstract IdToEntityMap getEntities(String[] keys) throws Exception;

    @SuppressWarnings("unchecked")
    public EntityMap getIdMap(String id) throws Exception {
        return getEntities(new String[]{id}).get(id);
    }

    public double currentPacketUsage(String userId, String name) throws Exception {
        String todayString = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());

        EntityMap idMap = getIdMap("[" + userId + "]");
        Map<String, Object> nameMap = idMap == null ? null : idMap.getPersisterMap(name);
        String lifetime = getNested(nameMap, String.class, "lifetime");

        return lifetime != null && lifetime.equals(todayString) ?
                getNested(nameMap, Number.class, "value", "使用量").doubleValue() :
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

    public static class IdToEntityMap extends HashMap<String, EntityMap> {
    }

    public static abstract class EntityMap extends HashMap<String, Object> {
        public EntityMap() {
            super();
        }

        public EntityMap(Map<? extends String, ?> m) {
            super(m);
        }

        public abstract Map<String, Object> getPersisterMap(String persisterName);
    }

    static class DatastoreEntityMap extends EntityMap {
        public DatastoreEntityMap() {
            super();
        }

        @Override
        public Map<String, Object> getPersisterMap(String persisterName) {
            return getNested(this, Map.class, "value", persisterName);
        }
    }

    static class MemorystoreEntityMap extends EntityMap {
        public MemorystoreEntityMap() {
            super();
        }

        public MemorystoreEntityMap(Map<? extends String, ?> m) {
            super(m);
        }

        @Override
        public Map<String, Object> getPersisterMap(String persisterName) {
            return getNested(this, Map.class, persisterName);
        }
    }
}

class DatastoreExtractor extends PersisterExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(DatastoreExtractor.class);

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
    public IdToEntityMap getAll() throws Exception {
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

        IdToEntityMap result = new IdToEntityMap();
        mapList.forEach(jsonMap -> {
            DatastoreEntityMap valueMap = new DatastoreEntityMap();
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
    public IdToEntityMap getEntities(String[] keys) throws Exception {
        // Datastoreの方は、キーを絞って取得する機能はないので、全件取得してから絞る
        IdToEntityMap all = getAll();
        IdToEntityMap result = new IdToEntityMap();
        for (String key : keys) {
            if (all.containsKey(key)) {
                result.put(key, all.get(key));
            }
        }
        return result;
    }
}

class MemorystoreExtractor extends PersisterExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(MemorystoreExtractor.class);

    private static final String COMPUTE_ENGINE_ZONE = System.getenv("COMPUTE_ENGINE_ZONE");

    private static final String MEMORYSTORE_UTILS_DIRECTORY = System.getenv("MEMORYSTORE_UTILS_DIRECTORY");

    protected MemorystoreExtractor() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        super();
    }

    private static void execComputeEngineCommand(String command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder("gcloud", "compute", "ssh", "ci@memorystore-access", "--command=\"" + command + "\"", "--zone=" + COMPUTE_ENGINE_ZONE)
                .redirectErrorStream(true)
                .start();

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            String output = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
            throw new RuntimeException(command + "の実行中にエラーが発生しました:\n" + output);
        }
    }

    private static void downloadComputeEngineFile(String src, String dst) throws IOException, InterruptedException {
        Process process = new ProcessBuilder("gcloud", "compute", "scp", "ci@memorystore-access:" + src, dst, "--zone=" + COMPUTE_ENGINE_ZONE)
                .redirectErrorStream(true)
                .start();

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            String output = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
            throw new RuntimeException(src + "の実行中にエラーが発生しました:\n" + output);
        }
    }

    @Override
    public IdToEntityMap getAll() throws Exception {
        Path tempFile = Files.createTempFile(Paths.get("."), "memorystore_temp", ".txt");
        String tempFileName = tempFile.getFileName().toString();
        execComputeEngineCommand("cd ~/" + MEMORYSTORE_UTILS_DIRECTORY + " && ./queryAll.sh " + tempFileName);
        downloadComputeEngineFile("~/" + MEMORYSTORE_UTILS_DIRECTORY + "/" + tempFileName, ".");

        IdToEntityMap result = fileToMap(tempFile);

        Files.deleteIfExists(tempFile);

        return result;
    }

    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile("^key=(.*?),value=(.*)$");

    private static IdToEntityMap fileToMap(Path file) throws IOException {
        IdToEntityMap result = new IdToEntityMap();

        Files.readAllLines(file, StandardCharsets.UTF_8).forEach(line -> {
            Matcher matcher = KEY_VALUE_PATTERN.matcher(line);
            if (matcher.find()) {
                String id = matcher.group(1);
                String valueString = matcher.group(2);
                try {
                    EntityMap valueMap = new ObjectMapper().readValue(valueString, new TypeReference<EntityMap>() {
                    });
                    result.put(id, valueMap);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        });

        return result;
    }

    @Override
    public IdToEntityMap getEntities(String[] keys) throws Exception {
        IdToEntityMap result;

        switch (keys.length) {
            case 0:
                result = new IdToEntityMap();
                break;
            case 1:
                Path tempFile = Files.createTempFile(Paths.get("."), "memorystore_temp", ".txt");
                String tempFileName = tempFile.getFileName().toString();
                execComputeEngineCommand("cd ~/" + MEMORYSTORE_UTILS_DIRECTORY + " && ./queryKey.sh " + tempFileName + " " + keys[0].replaceAll("^\\[(.*)\\]$", "$1"));
                downloadComputeEngineFile("~/" + MEMORYSTORE_UTILS_DIRECTORY + "/" + tempFileName, ".");

                result = fileToMap(tempFile);

                Files.deleteIfExists(tempFile);

                break;
            default:
                // 全件取得してから絞る
                IdToEntityMap all = getAll();
                result = new IdToEntityMap();
                for (String key : keys) {
                    if (all.containsKey(key)) {
                        result.put(key, all.get(key));
                    }
                }

                break;
        }

        LOG.debug("Memorystore = {}", result.toString());

        return result;
    }
}
