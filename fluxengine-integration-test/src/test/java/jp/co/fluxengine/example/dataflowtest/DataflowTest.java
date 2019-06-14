package jp.co.fluxengine.example.dataflowtest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.fluxengine.example.plugin.read.DailyDataReader;
import jp.co.fluxengine.publisher.CsvPublisher;
import jp.co.fluxengine.publisher.JsonPublisher;
import jp.co.fluxengine.publisher.ReadPublisher;
import jp.co.fluxengine.remote.test.CloudStoreSelecter;
import jp.co.fluxengine.remote.test.RemoteRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
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

    private static final Logger LOG = LogManager.getLogger();

    private static String topic;

    private static String persisterNamespace;
    private static String persisterKind;

    @BeforeAll
    static void setup() throws IOException {
        String projectId = System.getenv("PROJECT");
        RemoteRunner.setProjectId(projectId);

        Properties envProps = loadProperties("/dataflow_job_publisher_sample.env");
        topic = envProps.getProperty("FROM_TOPIC");

        Properties persisterProps = loadProperties("/persisterDataStore.properties");
        persisterNamespace = persisterProps.getProperty("namespace");
        persisterKind = persisterProps.getProperty("kind");

        LOG.debug("topic = {}, namespace = {}, kind = {}", topic, persisterNamespace, persisterKind);
    }

    private static Properties loadProperties(String resourcePath) throws IOException {
        try (InputStream in = DataflowTest.class.getResourceAsStream(resourcePath)) {
            Properties props = new Properties();
            props.load(in);
            return props;
        }
    }

    @Test
    void testDataflow() throws IOException, InterruptedException {
        // persisterの現在の値を取得する
        double usageBefore = currentPacketUsage();

        // テストデータをDataflowのジョブに流す
        URL resourceURL = getClass().getResource("/dataflow_test_data.json");
        String inputJsonString = IOUtils.toString(resourceURL, "UTF-8");
        LOG.debug("input = " + inputJsonString);

        RemoteRunner.publishOneTime(inputJsonString, topic);

        // 処理完了を待つ
        Thread.sleep(10000);

        // 結果のassertionを行う
        assertThat(getResultJson()).anySatisfy(json -> {
            assertThat(json.get("id").asText()).isEqualTo("[uid12345, persister/パケット積算データ#パケット積算データ]");
            assertThat(json.get("value").get("使用量").asDouble()).isEqualTo(usageBefore + 500);
        }).anySatisfy(json -> {
            assertThat(json.get("id").asText()).isEqualTo("[uid12345, rule/パケット積算#状態遷移]");
            assertThat(json.get("value").get("currentState").asText()).isEqualTo("s2");
        });
    }

    @Test
    void testEventPublisherAndTransaction() throws IOException, NoSuchMethodException, InterruptedException {
        // persisterの現在の値を取得する
        double usageBefore = currentPacketUsage();

        // EventPublisherを使ってテストデータをDataflowのジョブに流す
        CsvPublisher csvPublisher = new CsvPublisher("input/event.csv");
        csvPublisher.publish();

        String eventJson = "[{\"eventName\":\"パケットイベント\", \"namespace\":\"event/パケットイベント\", \"createTime\":null, \"property\":{\"端末ID\":\"C01\",\"日時\":\"2018/11/10 00:00:01\",\"使用量\":500}}] ";
        JsonPublisher jsonPublisher = new JsonPublisher(eventJson);
        jsonPublisher.publish();

        Class<?> readClazz = DailyDataReader.class;
        Method method = readClazz.getMethod("getList", String.class);
        ReadPublisher readPublisher = new ReadPublisher("event/パケットイベント", "パケットイベント", readClazz, method, new Object[]{"C01"});
        readPublisher.publish();

        // 競合によるリトライが発生するので、長く待つ
        Thread.sleep(30000);

        // パケット積算データが2600増えているはず
        assertThat(currentPacketUsage()).isEqualTo(usageBefore + 2600);
    }

    private static double currentPacketUsage() throws IOException {
        String todayString = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        return Arrays.stream(getResultJson()).filter(json -> {
            JsonNode id = json.get("id");
            JsonNode lifetime = json.get("lifetime");
            return id != null && id.asText().equals("[uid12345, persister/パケット積算データ#パケット積算データ]")
                    && lifetime != null && lifetime.asText().equals(todayString);
        }).map(json -> json.get("value").get("使用量").asDouble()).findFirst().orElse(0.0);
    }

    private static JsonNode[] getResultJson() throws IOException {
        File resultFile = File.createTempFile("persister", ".txt");

        CloudStoreSelecter persister = new CloudStoreSelecter(persisterNamespace, persisterKind);
        persister.exportEntityDataToFile(resultFile.getAbsolutePath());

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
