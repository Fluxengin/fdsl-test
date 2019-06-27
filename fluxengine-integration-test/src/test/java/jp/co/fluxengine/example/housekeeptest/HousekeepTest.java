package jp.co.fluxengine.example.housekeeptest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

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
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

// このクラスは、Datastoreを変更するので、
// 普段は実行されないようにし、
// 環境変数"FLUXENGINE_INTEGRATION_TEST_MODE"が"HOUSEKEEP"のときだけ実行できるようにしている
// CI/CDで実行されることを想定したテストクラス
@EnabledIfEnvironmentVariable(named = "FLUXENGINE_INTEGRATION_TEST_MODE", matches = "HOUSEKEEP|housekeep")
public class HousekeepTest {

    private static final Logger LOG = LogManager.getLogger();

    private static String persisterNamespace;
    private static String persisterKind;

    private static Class<?> cloudStoreSelecterClass;

    private static OkHttpClient client;
    private static String housekeepUrl;

    @BeforeAll
    static void setup() throws Exception {
        ClassLoader remoteTestRunnerLoader = getClassLoaderFromLib("fluxengine-remote-test-runner-.+\\.jar");

        cloudStoreSelecterClass = remoteTestRunnerLoader.loadClass("jp.co.fluxengine.remote.test.CloudStoreSelecter");

        Properties persisterProps = loadProperties("/persisterDataStore.properties");
        persisterNamespace = persisterProps.getProperty("namespace");
        persisterKind = persisterProps.getProperty("kind");
        String projectId = persisterProps.getProperty("projectId");

        LOG.debug("namespace = {}, kind = {}", persisterNamespace, persisterKind);

        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        housekeepUrl = "https://" + projectId + ".appspot.com/fluxengine-dataflow-housekeep";
    }

    private static Properties loadProperties(String resourcePath) throws IOException {
        try (InputStream in = HousekeepTest.class.getResourceAsStream(resourcePath)) {
            Properties props = new Properties();
            props.load(in);
            return props;
        }
    }

    private static ClassLoader getClassLoaderFromLib(String jarNameRegex) throws MalformedURLException {
        File jarFile = Arrays.stream(new File("lib").listFiles())
                .filter(file -> file.getName().matches(jarNameRegex))
                .findAny()
                .orElseThrow(() -> new RuntimeException(jarNameRegex + " にマッチするjarが見つかりませんでした"));
        return new URLClassLoader(new URL[]{jarFile.toURI().toURL()});
    }

    @Test
    void testAfterHousekeep() throws Exception {
        // 既にテストデータが投入されている前提
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        String todayString = today.format(formatter);
        String tomorrowString = tomorrow.format(formatter);

        // Housekeep実行前の状態のassetionを行う
        JsonNode[] before = getResultJson();
        assertThat(before).anySatisfy(json -> {
            assertThat(json.get("id").asText()).isEqualTo("[user1]");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算A").get("value").get("使用量").isNumber()).isTrue();
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算A").get("lifetime").asText()).isEmpty();
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態A").get("value").get("currentState").asText()).isEqualTo("s2");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態A").get("lifetime").asText()).isEmpty();
            assertThat(json.get("lifetime").asText()).isEmpty();
        }).anySatisfy(json -> {
            assertThat(json.get("id").asText()).isEqualTo("[user2]");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算B").get("value").get("使用量").isNumber()).isTrue();
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算B").get("lifetime").asText()).isEqualTo(todayString);
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態B").get("value").get("currentState").asText()).isEqualTo("s2");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態B").get("lifetime").asText()).isEmpty();
            assertThat(json.get("lifetime").asText()).isEmpty();
        }).anySatisfy(json -> {
            assertThat(json.get("id").asText()).isEqualTo("[user3]");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算C").get("value").get("使用量").isNumber()).isTrue();
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算C").get("lifetime").asText()).isEmpty();
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態C").get("value").get("currentState").asText()).isEqualTo("s2");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態C").get("lifetime").asText()).isEqualTo(todayString);
            assertThat(json.get("lifetime").asText()).isEmpty();
        }).anySatisfy(json -> {
            assertThat(json.get("id").asText()).isEqualTo("[user4]");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算D").get("value").get("使用量").isNumber()).isTrue();
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算D").get("lifetime").asText()).isEqualTo(todayString);
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態D").get("value").get("currentState").asText()).isEqualTo("s2");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態D").get("lifetime").asText()).isEqualTo(todayString);
            assertThat(json.get("lifetime").asText()).isEqualTo(todayString);
        }).anySatisfy(json -> {
            assertThat(json.get("id").asText()).isEqualTo("[user5]");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算D").get("value").get("使用量").isNumber()).isTrue();
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算D").get("lifetime").asText()).isEqualTo(todayString);
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態D").get("value").get("currentState").asText()).isEqualTo("s2");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態D").get("lifetime").asText()).isEqualTo(tomorrowString);
            assertThat(json.get("lifetime").asText()).isEqualTo(tomorrowString);
        });

        // ここでHousekeepを実行する
        // 既にHousekeepのServletがデプロイされている前提
        assertThat(executeHousekeep()).isTrue();

        // バッチの実行終了を待つ
        Thread.sleep(120000);

        // Housekeep実行後の状態のassetionを行う
        JsonNode[] after = getResultJson();
        assertThat(after).anySatisfy(json -> {
            assertThat(json.get("id").asText()).isEqualTo("[user1]");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算A").get("value").get("使用量").isNumber()).isTrue();
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算A").get("lifetime").asText()).isEmpty();
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態A").get("value").get("currentState").asText()).isEqualTo("s2");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態A").get("lifetime").asText()).isEmpty();
            assertThat(json.get("lifetime").asText()).isEmpty();
        }).anySatisfy(json -> {
            assertThat(json.get("id").asText()).isEqualTo("[user2]");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算B").get("value").get("使用量").isNumber()).isTrue();
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算B").get("lifetime").asText()).isEqualTo(todayString);
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態B").get("value").get("currentState").asText()).isEqualTo("s2");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態B").get("lifetime").asText()).isEmpty();
            assertThat(json.get("lifetime").asText()).isEmpty();
        }).anySatisfy(json -> {
            assertThat(json.get("id").asText()).isEqualTo("[user3]");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算C").get("value").get("使用量").isNumber()).isTrue();
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算C").get("lifetime").asText()).isEmpty();
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態C").get("value").get("currentState").asText()).isEqualTo("s2");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態C").get("lifetime").asText()).isEqualTo(todayString);
            assertThat(json.get("lifetime").asText()).isEmpty();
        }).noneSatisfy(json -> {
            assertThat(json.get("id").asText()).isEqualTo("[user4]");
        }).anySatisfy(json -> {
            assertThat(json.get("id").asText()).isEqualTo("[user5]");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算D").get("value").get("使用量").isNumber()).isTrue();
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeepパケット積算D").get("lifetime").asText()).isEqualTo(todayString);
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態D").get("value").get("currentState").asText()).isEqualTo("s2");
            assertThat(json.get("value").get("housekeep/様々なlifetimeで永続化#Housekeep状態D").get("lifetime").asText()).isEqualTo(tomorrowString);
            assertThat(json.get("lifetime").asText()).isEqualTo(tomorrowString);
        });
    }

    private static boolean executeHousekeep() throws IOException {
        Request request = new Request.Builder().url(housekeepUrl).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) {
                LOG.info("got no response");
            } else {
                LOG.info("got response: " + response.body().string());
            }
            return response.isSuccessful();
        } catch (IOException e) {
            LOG.error("exception occurred", e);
            throw e;
        }
    }

    private static JsonNode[] getResultJson() throws Exception {
        File resultFile = File.createTempFile("housekeep", ".txt");

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
