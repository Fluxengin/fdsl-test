package jp.co.fluxengine.example.dataflowtest;

import jp.co.fluxengine.example.CloudSqlPool;
import jp.co.fluxengine.example.plugin.read.DailyDataReader;
import jp.co.fluxengine.example.util.PersisterExtractor;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.db.type.Changes;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.lang.reflect.Method;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

// このクラスは、Dataflowのジョブにデータを投入するので、
// 普段は実行されないようにし、
// 環境変数"FLUXENGINE_INTEGRATION_TEST_MODE"が"DATAFLOW"のときだけ実行できるようにしている
// CI/CDで実行されることを想定したテストクラス
@EnabledIfEnvironmentVariable(named = "FLUXENGINE_INTEGRATION_TEST_MODE", matches = "DATAFLOW|dataflow")
public class DataflowTest extends PersisterExtractor {

    private static final Logger LOG = LogManager.getLogger(DataflowTest.class);

    @Test
    void testDataflow() throws Exception {
        LOG.info("testDataflow 開始");
        // persisterの現在の値を取得する
        double usageBefore = currentPacketUsage("uid12345", "persister/パケット積算データ#パケット積算データ");

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
        double usageBefore = currentPacketUsage("uid12345", "persister/パケット積算データ#パケット積算データ");

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
        assertThat(currentPacketUsage("uid12345", "persister/パケット積算データ#パケット積算データ")).isEqualTo(usageBefore + 2600);
        LOG.info("testEventPublisherAndTransaction 終了");
    }

    @Test
    void testEffector() throws Exception {
        LOG.info("testEffector 開始");
        Table targetTable = new Table(CloudSqlPool.getDataSource(), "integration_test_effector");
        Changes changes = new Changes(targetTable);

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

}
