package jp.co.fluxengine.example.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Properties;

public abstract class PersisterExtractor {

    private static final Logger LOG = LogManager.getLogger(PersisterExtractor.class);

    protected static String projectId = System.getenv("PROJECT");

    protected static String topic;

    protected static String persisterNamespace;
    protected static String persisterKind;

    protected static Class<?> remoteRunnerClass;
    protected static Class<?> cloudStoreSelecterClass;

    @BeforeAll
    static void setup() throws Exception {
        LOG.info("setup 開始");
        String projectId = System.getenv("PROJECT");

        ClassLoader remoteTestRunnerLoader = getClassLoaderFromLib("fluxengine-remote-test-runner-.+\\.jar");

        remoteRunnerClass = remoteTestRunnerLoader.loadClass("jp.co.fluxengine.remote.test.RemoteRunner");
        cloudStoreSelecterClass = remoteTestRunnerLoader.loadClass("jp.co.fluxengine.remote.test.CloudStoreSelecter");

        remoteRunnerClass.getDeclaredMethod("setProjectId", String.class).invoke(null, projectId);

        Properties envProps = loadProperties("/publisher.properties");
        topic = envProps.getProperty("totopic");

        Properties persisterProps = loadProperties("/persisterDataStore.properties");
        persisterNamespace = persisterProps.getProperty("namespace");
        persisterKind = persisterProps.getProperty("kind");

        LOG.debug("topic = {}, namespace = {}, kind = {}", topic, persisterNamespace, persisterKind);
        LOG.info("setup 終了");
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

    public double currentPacketUsage(String userId, String name) throws Exception {
        String todayString = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        return Arrays.stream(getResultJson()).filter(json -> {
            try {
                JsonNode id = json.get("id");
                JsonNode lifetime = json.get("value").get(name).get("lifetime");
                return json.get("id").asText().equals("[" + userId + "]")
                        && json.get("value").get(name).get("lifetime").asText().equals(todayString);
            } catch (NullPointerException e) {
                return false;
            }
        }).map(json -> json.get("value").get(name).get("value").get("使用量").asDouble())
                .findFirst().orElse(0.0);
    }

    public JsonNode[] getResultJson() throws Exception {
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

        return new ObjectMapper().readValue(resultJsonString, JsonNode[].class);
    }

}
