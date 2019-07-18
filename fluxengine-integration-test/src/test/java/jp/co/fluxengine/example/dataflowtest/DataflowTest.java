package jp.co.fluxengine.example.dataflowtest;

import jp.co.fluxengine.example.CloudSqlPool;
import jp.co.fluxengine.example.plugin.read.PublishDataReader;
import jp.co.fluxengine.example.util.EnabledIfPersisterDbIs;
import jp.co.fluxengine.example.util.PersisterExtractor;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.db.type.Changes;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static jp.co.fluxengine.example.util.PersisterExtractor.getClassLoaderFromLib;
import static jp.co.fluxengine.example.util.PersisterExtractor.getNested;
import static org.assertj.core.api.Assertions.assertThat;

// このクラスは、Dataflowのジョブにデータを投入するので、
// 普段は実行されないようにし、
// 環境変数"FLUXENGINE_INTEGRATION_TEST_MODE"が"DATAFLOW"のときだけ実行できるようにしている
// CI/CDで実行されることを想定したテストクラス
@EnabledIfEnvironmentVariable(named = "FLUXENGINE_INTEGRATION_TEST_MODE", matches = "DATAFLOW|dataflow")
public class DataflowTest {

    private static final Logger LOG = LogManager.getLogger(DataflowTest.class);

    private static PersisterExtractor extractor;

    @BeforeAll
    static void setup() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        extractor = PersisterExtractor.getInstance();
    }

    @Test
    void testDataflow() throws Exception {
        LOG.info("testDataflow 開始");
        // persisterの現在の値を取得する
        double usageBefore = extractor.currentPacketUsage("uid12345", "persister/パケット積算データ#パケット積算データ");

        // テストデータをDataflowのジョブに流す
        URL resourceURL = getClass().getResource("/dataflow_test_data.json");
        String inputJsonString = IOUtils.toString(resourceURL, "UTF-8");
        LOG.debug("input = " + inputJsonString);

        LOG.info("testDataflow データ送信");
        extractor.publishOneTime(inputJsonString);

        // 処理完了を待つ
        LOG.info("testDataflow 待機");
        Thread.sleep(20000);
        LOG.info("testDataflow 待機終了");

        // 結果のassertionを行う
        Map<String, Object> result = extractor.getResultJson("[uid12345]");
        assertThat(getNested(result, double.class, "value", "persister/パケット積算データ#パケット積算データ", "value", "使用量")).isEqualTo(usageBefore + 500);
        assertThat(getNested(result, String.class, "value", "rule/パケット積算#状態遷移", "value", "currentState")).isEqualTo("s2");

        LOG.info("testDataflow 終了");
    }

    @Test
    @EnabledIfPersisterDbIs("memorystore")
    void testEventPublisherAndTransaction() throws Exception {
        LOG.info("testEventPublisherAndTransaction 開始");
        // persisterの現在の値を取得する
        double usageBefore = extractor.currentPacketUsage("publish", "persister/パケット積算データ#パケット積算データ");

        ClassLoader eventPublisherLoader = getClassLoaderFromLib("fluxengine-event-publisher-.+\\.jar");

        // EventPublisherを使ってテストデータをDataflowのジョブに流す
        Class<?> csvPublisherClass = eventPublisherLoader.loadClass("jp.co.fluxengine.publisher.CsvPublisher");
        Object csvPublisher = csvPublisherClass.getConstructor(String.class).newInstance("input/event.csv");
        csvPublisherClass.getMethod("publish").invoke(csvPublisher);

        String eventJson = "[{\"eventName\":\"パケットイベント\", \"namespace\":\"event/パケットイベント\", \"createTime\":null, \"property\":{\"端末ID\":\"publish\",\"日時\":\"2018/11/10 00:00:01\",\"使用量\":500}}]";
        Class<?> jsonPublisherClass = eventPublisherLoader.loadClass("jp.co.fluxengine.publisher.JsonPublisher");
        Object jsonPublisher = jsonPublisherClass.getConstructor(String.class).newInstance(eventJson);
        jsonPublisherClass.getMethod("publish").invoke(jsonPublisher);

        Class<?> readClazz = PublishDataReader.class;
        Method method = readClazz.getMethod("getList", String.class);
        Class<?> readPublisherClass = eventPublisherLoader.loadClass("jp.co.fluxengine.publisher.ReadPublisher");
        Object readPublisher = readPublisherClass.getConstructor(String.class, String.class, Class.class, Method.class, Object[].class).newInstance("event/パケットイベント", "パケットイベント", readClazz, method, new Object[]{"publish"});
        readPublisherClass.getMethod("publish").invoke(readPublisher);

        // 競合によるリトライが発生するので、長く待つ
        LOG.info("testEventPublisherAndTransaction 待機");
        Thread.sleep(50000);
        LOG.info("testEventPublisherAndTransaction 待機終了");

        // パケット積算データが2600増えているはず
        assertThat(extractor.currentPacketUsage("publish", "persister/パケット積算データ#パケット積算データ")).isEqualTo(usageBefore + 2600);
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
        extractor.publishOneTime(eventJson);

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

    @Test
    void testSubscription() throws Exception {
        LOG.info("testSubscription 開始");

        String targetUserId = System.getenv("TEST_PERSIST_USERID");
        String targetString = System.getenv("TEST_PERSIST_STRING");

        Map<String, Object> result = extractor.getResultJson("[" + targetUserId + "]");
        assertThat(getNested(result, String.class, "value", "subscription/イベントの文字列をそのまま永続化#Subscriptionイベント永続化", "value", "文字列")).isEqualTo(targetString);

        LOG.info("testSubscription 終了");
    }

}
