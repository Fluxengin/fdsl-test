package jp.co.fluxengine.example.dataflowtest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.fluxengine.example.CloudSqlPool;
import jp.co.fluxengine.example.plugin.read.DailyDataReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.db.type.Changes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

// このクラスは、Dataflowのジョブにデータを投入するので、
// 普段は実行されないようにし、
// 環境変数"FLUXENGINE_INTEGRATION_TEST_MODE"が"DATAFLOW"のときだけ実行できるようにしている
// CI/CDで実行されることを想定したテストクラス
@EnabledIfEnvironmentVariable(named = "FLUXENGINE_INTEGRATION_TEST_MODE", matches = "DATAFLOW|dataflow")
public class DataflowTest {

    private static final Logger LOG = LogManager.getLogger(DataflowTest.class);

    private static String topic;

    private static String persisterNamespace;
    private static String persisterKind;

    private static Class<?> remoteRunnerClass;
    private static Class<?> cloudStoreSelecterClass;

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

    private static Properties loadProperties(String resourcePath) throws IOException {
        try (InputStream in = DataflowTest.class.getResourceAsStream(resourcePath)) {
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
    void testDataflow() throws Exception {
        LOG.info("testDataflow 開始");
        // persisterの現在の値を取得する
        double usageBefore = currentPacketUsage();

        // テストデータをDataflowのジョブに流す
        URL resourceURL = getClass().getResource("/dataflow_test_data.json");
        String inputJsonString = IOUtils.toString(resourceURL, "UTF-8");
        LOG.debug("input = " + inputJsonString);

        LOG.info("testDataflow データ送信");
        remoteRunnerClass.getDeclaredMethod("publishOneTime", String.class, String.class).invoke(null, inputJsonString, topic);

        // 処理完了を待つ
        LOG.info("testDataflow 待機");
        Thread.sleep(20000);
        LOG.info("testDataflow 待機終了");

        // 結果のassertionを行う
        assertThat(getResultJson()).anySatisfy(json -> {
            assertThat(json.get("id").asText()).isEqualTo("[uid12345]");
            assertThat(json.get("value").get("persister/パケット積算データ#パケット積算データ").get("value").get("使用量").asDouble()).isEqualTo(usageBefore + 500);
            assertThat(json.get("value").get("rule/パケット積算#状態遷移").get("value").get("currentState").asText()).isEqualTo("s2");
        });
        LOG.info("testDataflow 終了");
    }

    @Test
    void testEventPublisherAndTransaction() throws Exception {
        LOG.info("testEventPublisherAndTransaction 開始");
        // persisterの現在の値を取得する
        double usageBefore = currentPacketUsage();

        ClassLoader eventPublisherLoader = getClassLoaderFromLib("fluxengine-event-publisher-.+\\.jar");

        // EventPublisherを使ってテストデータをDataflowのジョブに流す
        Class<?> csvPublisherClass = eventPublisherLoader.loadClass("jp.co.fluxengine.publisher.CsvPublisher");
        Object csvPublisher = csvPublisherClass.getConstructor(String.class).newInstance("input/event.csv");
        csvPublisherClass.getMethod("publish").invoke(csvPublisher);

        String eventJson = "[{\"eventName\":\"パケットイベント\", \"namespace\":\"event/パケットイベント\", \"createTime\":null, \"property\":{\"端末ID\":\"C01\",\"日時\":\"2018/11/10 00:00:01\",\"使用量\":500}}]";
        Class<?> jsonPublisherClass = eventPublisherLoader.loadClass("jp.co.fluxengine.publisher.JsonPublisher");
        Object jsonPublisher = jsonPublisherClass.getConstructor(String.class).newInstance(eventJson);
        jsonPublisherClass.getMethod("publish").invoke(jsonPublisher);


        Class<?> readClazz = DailyDataReader.class;
        Method method = readClazz.getMethod("getList", String.class);
        Class<?> readPublisherClass = eventPublisherLoader.loadClass("jp.co.fluxengine.publisher.ReadPublisher");
        Object readPublisher = readPublisherClass.getConstructor(String.class, String.class, Class.class, Method.class, Object[].class).newInstance("event/パケットイベント", "パケットイベント", readClazz, method, new Object[]{"C01"});
        readPublisherClass.getMethod("publish").invoke(readPublisher);

        // 競合によるリトライが発生するので、長く待つ
        LOG.info("testEventPublisherAndTransaction 待機");
        Thread.sleep(50000);
        LOG.info("testEventPublisherAndTransaction 待機終了");

        // パケット積算データが2600増えているはず
        assertThat(currentPacketUsage()).isEqualTo(usageBefore + 2600);
        LOG.info("testEventPublisherAndTransaction 終了");
    }

    @Test
    void testEffector() throws Exception {
        LOG.info("testEffector 開始");
        Changes changes = new Changes(CloudSqlPool.getDataSource());

        changes.setStartPointNow();

        // RemoteTestRunnerを使ってデータを流す
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");
        String message = "イベント送信タイムスタンプ: " + LocalDateTime.now().format(formatter);
        String eventJson = "[{\"eventName\":\"エフェクタ送信イベント\", \"namespace\":\"effector_check/エフェクタ動作確認\", \"createTime\":null, \"property\":{\"ユーザーID\":\"effector_check_01\",\"メッセージ\":\"" + message + "\"}}]";
        LOG.info("testEffector データ送信");
        remoteRunnerClass.getDeclaredMethod("publishOneTime", String.class, String.class).invoke(null, eventJson, topic);

        // Dataflowが処理完了するまで少し待つ
        LOG.info("testEffector 待機");
        Thread.sleep(20000);
        LOG.info("testEffector 待機終了");

        changes.setEndPointNow();

        // 結果のassertionを行う
        org.assertj.db.api.Assertions.assertThat(changes)
                .ofCreationOnTable("integration_test_effector").hasNumberOfChanges(1)
                .changeOnTable("integration_test_effector")
                .isCreation()
                .rowAtEndPoint()
                .value("message").isEqualTo(message);
        LOG.info("testEffector 終了");
    }

    private static double currentPacketUsage() throws Exception {
        String todayString = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        return Arrays.stream(getResultJson()).filter(json -> {
            JsonNode id = json.get("id");
            JsonNode lifetime = json.get("lifetime");
            return id != null && id.asText().equals("[uid12345]")
                    && lifetime != null && lifetime.asText().equals(todayString);
        }).map(json -> json.get("value").get("persister/パケット積算データ#パケット積算データ").get("value").get("使用量").asDouble())
                .findFirst().orElse(0.0);
    }

    private static JsonNode[] getResultJson() throws Exception {
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
