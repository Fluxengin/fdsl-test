package jp.co.fluxengine.example.dataflowtest;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;
import jp.co.fluxengine.remote.test.CloudStoreSelecter;
import jp.co.fluxengine.remote.test.RemoteRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

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
    // テストデータをDataflowのジョブに流す
    URL resourceURL = getClass().getResource("/dataflow_test_data.json");
    String inputJsonString = IOUtils.toString(resourceURL, "UTF-8");
    LOG.debug("input = " + inputJsonString);

    RemoteRunner.publishOneTime(inputJsonString, topic);

    // 処理完了を待つ
    Thread.sleep(5000);

    // 結果をファイルに取得する
    CloudStoreSelecter persister = new CloudStoreSelecter(persisterNamespace, persisterKind);
    persister.exportEntityDataToFile("dataflow_test_result.txt");

    // ファイルから結果をJSONとして取得する
    String resultJsonString =
        "[" + FileUtils
            .readFileToString(new File("dataflow_test_result.txt"), Charset.defaultCharset()).trim()
            .replace('\n', ',') + "]";
    LOG.debug("result = " + resultJsonString);
    JsonNode[] resultArray = new ObjectMapper().readValue(resultJsonString, JsonNode[].class);

    // JSONのassertionを行う
    assertThat(resultArray).anyMatch(json ->
        json.get("id").asText().equals("[uid12345, persister/パケット積算データ#パケット積算データ]")
            && json.get("value").get("使用量").asDouble() == 500).anyMatch(json ->
        json.get("id").asText().equals("[uid12345, rule/パケット積算#状態遷移]")
            && json.get("value").get("currentState").asText().equals("s2"));
  }
}
