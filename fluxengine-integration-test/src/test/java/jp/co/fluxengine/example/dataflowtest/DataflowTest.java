package jp.co.fluxengine.example.dataflowtest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jp.co.fluxengine.example.plugin.read.PublishDataReader;
import jp.co.fluxengine.example.util.PersisterExtractor;
import jp.co.fluxengine.example.util.Utils;
import jp.co.fluxengine.stateengine.model.datom.Event;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import static jp.co.fluxengine.example.util.PersisterExtractor.getClassLoaderFromLib;
import static jp.co.fluxengine.example.util.Utils.getNested;
import static org.assertj.core.api.Assertions.assertThat;

// このクラスは、Dataflowのジョブにデータを投入するので、
// 普段は実行されないようにし、
// 環境変数"FLUXENGINE_INTEGRATION_TEST_DATAFLOW_STREAM"が"true"のときだけ実行できるようにしている
// CI/CDで実行されることを想定したテストクラス
@EnabledIfEnvironmentVariable(named = "FLUXENGINE_INTEGRATION_TEST_DATAFLOW_STREAM", matches = "true|TRUE")
public class DataflowTest {

    private static final Logger LOG = LoggerFactory.getLogger(DataflowTest.class);

    private static PersisterExtractor extractor;

    private static String testIdForMySQL;

    private static String timezoneId;

    @BeforeEach
    void logStart(TestInfo testinfo) {
        LOG.info("{} 開始", testinfo.getDisplayName());
    }

    @AfterEach
    void logEnd(TestInfo testInfo) {
        LOG.info("{} 終了", testInfo.getDisplayName());
    }

    @BeforeAll
    static void before() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        extractor = PersisterExtractor.getInstance();
        testIdForMySQL = UUID.randomUUID().toString();
        timezoneId = PersisterExtractor.loadProperties("/dataflow_job_publisher_sample.env").getProperty("TIMEZONE");
        TimeZone.setDefault(TimeZone.getTimeZone(timezoneId));
    }

    @Test
    void testDataflow() throws Exception {
        // persisterの現在の値を取得する
        double usageBefore = extractor.currentPacketUsage("uid12345", "persister/パケット積算データ#パケット積算データ");

        // テストデータをDataflowのジョブに流す
        URL resourceURL = getClass().getResource("/dataflow_test_data.json");
        String inputJsonString = IOUtils.toString(resourceURL, StandardCharsets.UTF_8);
        LOG.debug("input = " + inputJsonString);

        LOG.info("testDataflow データ送信");
        extractor.publishEventString(inputJsonString);

        LOG.info("testDataflow 待機開始");
        Thread.sleep(50000);
        LOG.info("testDataflow 待機終了");

        // 結果のassertionを行う
        PersisterExtractor.EntityMap result = extractor.getIdMap("[uid12345]");
        assertThat(getNested(result.getPersisterMap("persister/パケット積算データ#パケット積算データ"), Number.class, "value", "使用量").doubleValue()).isEqualTo(usageBefore + 500);
        assertThat(getNested(result.getPersisterMap("rule/パケット積算#状態遷移"), String.class, "value", "currentState")).isEqualTo("s2");
    }

    @Test
    void testPersisterPut() throws Exception {
        URL resourceURL = getClass().getResource("/dataflow_persister_put_data.json");
        String inputJsonString = IOUtils.toString(resourceURL, StandardCharsets.UTF_8);
        LOG.debug("input = " + inputJsonString);

        LOG.info("testPersisterPut データ送信");
        extractor.publishEventString(inputJsonString);

        LOG.info("testPersisterPut 待機開始");
        Thread.sleep(20000);
        LOG.info("testPersisterPut 待機終了");

        // [PersisterRead01]に何も永続化されていないことを確認する
        Map<String, Object> result = extractor.getIdMap("[PersisterRead01]");
        assertThat(result).isNull();
    }

    @Test
    void testEventPublisherAndTransaction() throws Exception {
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

        LOG.info("testEventPublisherAndTransaction 待機開始");
        Thread.sleep(50000);
        LOG.info("testEventPublisherAndTransaction 待機終了");

        // パケット積算データが2600増えているはず
        assertThat(extractor.currentPacketUsage("publish", "persister/パケット積算データ#パケット積算データ")).isEqualTo(usageBefore + 2600);
    }

    @Test
    void testTransaction() throws Exception {
        extractor.publishEvent("transaction/複数のキーを同時更新", "e", LocalDateTime.now(), Utils.toMap(
                "attr1", 10,
                "attr2", 20
        ));
        extractor.publishEvent("transaction/複数のキーを同時更新", "e", LocalDateTime.now(), Utils.toMap(
                "attr1", 100,
                "attr2", 20
        ));

        LOG.info("testTransaction 待機開始");
        Thread.sleep(40000);
        LOG.info("testTransaction 待機終了");

        PersisterExtractor.IdToEntityMap entities = extractor.getEntities("[transaction_key1]", "[transaction_key2]");

        Map<String, Object> p1Map = entities.get("[transaction_key1]").getPersisterMap("transaction/複数のキーを同時更新#p1");
        assertThat(Utils.getNested(p1Map, Number.class, "value", "contents1")).isNotNull().satisfies(n -> assertThat(n.intValue()).isEqualTo(110));

        Map<String, Object> p2Map = entities.get("[transaction_key2]").getPersisterMap("transaction/複数のキーを同時更新#p2");
        assertThat(Utils.getNested(p2Map, Number.class, "value", "contents2")).isNotNull().satisfies(n -> assertThat(n.intValue()).isEqualTo(20));
    }

    @Test
    void testEffector() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");
        String message = "イベント送信タイムスタンプ: " + LocalDateTime.now().format(formatter);
        String eventJson = "[{\"eventName\":\"エフェクタ送信イベント\", \"namespace\":\"effector_check/エフェクタ動作確認\", \"createTime\":null, \"property\":{\"ユーザーID\":\"effector_check_01\",\"メッセージ\":\"" + message + "\"}}]";

        extractor.publishEventString(eventJson);

        String event2Message = UUID.randomUUID().toString();
        int event2Number = 1;
        extractor.publishEvent("effector_check/エフェクタ動作確認", "エフェクタ送信イベント2", LocalDateTime.now(), Utils.toMap(
                "ユーザID", "effector_check_02",
                "数値", event2Number,
                "メッセージ", event2Message
        ));

        // Dataflowが処理完了するまで少し待つ
        LOG.info("testEffector 待機");
        Thread.sleep(40000);
        LOG.info("testEffector 待機終了");

        // 結果のassertionを行う
        Utils.withTestDb(testDbConnection -> {
            // message を持つ行が、1行あればOK
            PreparedStatement selectCount = testDbConnection.prepareStatement(
                    "SELECT COUNT(*) FROM integration_test_effector WHERE userid = ? AND message = ?"
            );
            selectCount.setString(1, "effector_check_01");
            selectCount.setString(2, message);

            try (ResultSet countResult = selectCount.executeQuery()) {
                assertThat(countResult.next()).isTrue();
                assertThat(countResult.getInt(1)).isEqualTo(1);
            }

            selectCount.setString(1, "effector_check_02");
            selectCount.setString(2, event2Message + " : " + event2Number);

            try (ResultSet countResult2 = selectCount.executeQuery()) {
                assertThat(countResult2.next()).isTrue();
                assertThat(countResult2.getInt(1)).isEqualTo(1);
            }
        });
    }

    @Test
    void testSubscription() throws Exception {
        String targetUserId = System.getenv("TEST_PERSIST_USERID");
        String targetString = System.getenv("TEST_PERSIST_STRING");

        // testSubscription用のテストデータは事前に投入されているが、
        // それでも処理に時間がかかることがあるので、少し待つ
        LOG.info("testSubscription 待機");
        Thread.sleep(50000);
        LOG.info("testSubscription 待機終了");

        PersisterExtractor.EntityMap result = extractor.getIdMap("[" + targetUserId + "]");
        assertThat(getNested(result.getPersisterMap("subscription/イベントの文字列をそのまま永続化#Subscriptionイベント永続化"), String.class, "value", "文字列")).isEqualTo(targetString);
    }

    @Test
    void testGetMySQL() throws Exception {
        LOG.info("testGetMySQL 開始");

        // テストデータのINSERT
        Utils.withTestDb(testDbConnection -> {
            PreparedStatement insertTestRecords = testDbConnection.prepareStatement(
                    "INSERT INTO getmysql_test(test_id, id, bit_field, int_field, double_field, decimal_field, datetime_field, timestamp_field, varchar_field) " +
                            "VALUES " + StringUtils.repeat("(?,?,?,?,?,?,?,?,?)", ",", 7)
            );

            for (int i = 0; i < 7; i++) {
                insertTestRecords.setString(i * 9 + 1, testIdForMySQL);
                insertTestRecords.setLong(i * 9 + 2, i);
                insertTestRecords.setBoolean(i * 9 + 3, i % 2 == 0);
                insertTestRecords.setInt(i * 9 + 4, i + 1);
                insertTestRecords.setDouble(i * 9 + 5, i / 10.0);
                insertTestRecords.setBigDecimal(i * 9 + 6, new BigDecimal("100.00" + i));
                insertTestRecords.setTimestamp(i * 9 + 7, Timestamp.valueOf(String.format("2019-08-01 %1$02d:%1$02d:%1$02d", i)));
                insertTestRecords.setTimestamp(i * 9 + 8, Timestamp.valueOf(String.format("2019-08-02 %1$02d:%1$02d:%1$02d", i)));
                insertTestRecords.setString(i * 9 + 9, "test string " + i);
            }

            insertTestRecords.execute();

            PreparedStatement selectCountOfRecords = testDbConnection.prepareStatement("SELECT COUNT(*) FROM getmysql_test WHERE test_id = ?");
            selectCountOfRecords.setString(1, testIdForMySQL);
            try (ResultSet countResult = selectCountOfRecords.executeQuery()) {
                while (countResult.next()) {
                    LOG.debug("test_id = {} のレコードが {} 件あります", testIdForMySQL, countResult.getInt(1));
                }
            }
        });

        // できるだけテストを行うようにするため、3つのイベントをバラバラに投げる
        List<Event> eventList1 = Lists.newArrayList(createGetMySQLEvent(testIdForMySQL));
        List<Event> eventList2 = Lists.newArrayList(createGetMySQLEventWithAttributes(testIdForMySQL, 20, "getMySQL_条件分岐あり_20"));
        List<Event> eventList3 = Lists.newArrayList(createGetMySQLEventWithAttributes(testIdForMySQL, 60, "getMySQL_条件分岐あり_60"));

        LOG.info("testGetMySQL データ送信");
        extractor.publishEvents(eventList1);
        extractor.publishEvents(eventList2);
        // キャッシュONのテストのために、バリアントのキャッシュが作られるまで待つ
        Thread.sleep(30000);
        extractor.publishEvents(eventList3);

        LOG.info("testGetMySQL 待機開始");
        Thread.sleep(40000);
        LOG.info("testGetMySQL 待機終了");

        PersisterExtractor.IdToEntityMap resultMap = extractor.getEntities(
                "[getMySQL_条件分岐なしの単一属性]",
                "[getMySQL_条件分岐なしの複数属性]",
                "[getMySQL_条件分岐あり_20]",
                "[getMySQL_条件分岐あり_60]",
                "[getMySQL_param1つのSQL]",
                "[getMySQL_paramなしのSQL]",
                "[getMySQL_パラメタのあるvariant]"
        );

        Map<String, Object> noBranchOneAttribute = resultMap.get("[getMySQL_条件分岐なしの単一属性]").getPersisterMap("mysql/getMySQL#条件分岐なしの単一属性persister");
        assertThat(getNested(noBranchOneAttribute, String.class, "value", "value_string")).isEqualTo("test string 0");

        Map<String, Object> noBranchMultipleAttributes = resultMap.get("[getMySQL_条件分岐なしの複数属性]").getPersisterMap("mysql/getMySQL#条件分岐なしの複数属性persister");
        assertThat(getNested(noBranchMultipleAttributes, Boolean.class, "value", "bit_field")).isFalse();
        assertThat(getNested(noBranchMultipleAttributes, Number.class, "value", "int_field")).isEqualTo(2);
        assertThat(getNested(noBranchMultipleAttributes, Number.class, "value", "double_field")).isEqualTo(0.1);
        assertThat(getNested(noBranchMultipleAttributes, Number.class, "value", "decimal_field").doubleValue()).isEqualTo(100.001);
        // datetimeは、DatastoreだとString, MemorystoreだとLocalDateTimeで取得される
        assertThat(getNested(noBranchMultipleAttributes, Object.class, "value", "datetime_field")).isIn("2019-08-01T01:01:01", LocalDateTime.parse("2019-08-01T01:01:01"));
        assertThat(getNested(noBranchMultipleAttributes, Object.class, "value", "timestamp_field")).isIn("2019-08-02T01:01:01", LocalDateTime.parse("2019-08-02T01:01:01"));
        assertThat(getNested(noBranchMultipleAttributes, String.class, "value", "varchar_field")).isEqualTo("test string 1");

        PersisterExtractor.EntityMap branch20 = resultMap.get("[getMySQL_条件分岐あり_20]");
        Map<String, Object> branch20NoAttribute = branch20.getPersisterMap("mysql/getMySQL#条件分岐ありの単一属性persister");
        assertThat(getNested(branch20NoAttribute, Number.class, "value", "value_number")).isEqualTo(4);

        Map<String, Object> branch20MultipleAttributes = branch20.getPersisterMap("mysql/getMySQL#条件分岐ありの複数属性persister");
        assertThat(getNested(branch20MultipleAttributes, Boolean.class, "value", "bit_field")).isTrue();
        assertThat(getNested(branch20MultipleAttributes, Number.class, "value", "int_field")).isEqualTo(5);
        assertThat(getNested(branch20MultipleAttributes, Number.class, "value", "double_field")).isEqualTo(0.4);
        assertThat(getNested(branch20MultipleAttributes, Number.class, "value", "decimal_field").doubleValue()).isEqualTo(100.004);
        assertThat(getNested(branch20MultipleAttributes, Object.class, "value", "datetime_field")).isIn("2019-08-01T04:04:04", LocalDateTime.parse("2019-08-01T04:04:04"));
        assertThat(getNested(branch20MultipleAttributes, Object.class, "value", "timestamp_field")).isIn("2019-08-02T04:04:04", LocalDateTime.parse("2019-08-02T04:04:04"));
        assertThat(getNested(branch20MultipleAttributes, String.class, "value", "varchar_field")).isEqualTo("test string 4");

        PersisterExtractor.EntityMap branch60 = resultMap.get("[getMySQL_条件分岐あり_60]");
        Map<String, Object> branch60NoAttribute = branch60.getPersisterMap("mysql/getMySQL#条件分岐ありの単一属性persister");
        assertThat(getNested(branch60NoAttribute, Number.class, "value", "value_number")).isEqualTo(3);

        Map<String, Object> branch60MultipleAttributes = branch60.getPersisterMap("mysql/getMySQL#条件分岐ありの複数属性persister");
        assertThat(getNested(branch60MultipleAttributes, Boolean.class, "value", "bit_field")).isFalse();
        assertThat(getNested(branch60MultipleAttributes, Number.class, "value", "int_field")).isEqualTo(6);
        assertThat(getNested(branch60MultipleAttributes, Number.class, "value", "double_field")).isEqualTo(0.5);
        assertThat(getNested(branch60MultipleAttributes, Number.class, "value", "decimal_field").doubleValue()).isEqualTo(100.005);
        assertThat(getNested(branch60MultipleAttributes, Object.class, "value", "datetime_field")).isIn("2019-08-01T05:05:05", LocalDateTime.parse("2019-08-01T05:05:05"));
        assertThat(getNested(branch60MultipleAttributes, Object.class, "value", "timestamp_field")).isIn("2019-08-02T05:05:05", LocalDateTime.parse("2019-08-02T05:05:05"));
        assertThat(getNested(branch60MultipleAttributes, String.class, "value", "varchar_field")).isEqualTo("test string 5");

        Map<String, Object> oneParam = resultMap.get("[getMySQL_param1つのSQL]").getPersisterMap("mysql/getMySQL#param1つのSQLpersister");
        assertThat(getNested(oneParam, Boolean.class, "value", "bit_field")).isTrue();
        assertThat(getNested(oneParam, Number.class, "value", "int_field")).isEqualTo(1);
        assertThat(getNested(oneParam, Number.class, "value", "double_field")).isEqualTo(0.0);
        assertThat(getNested(oneParam, Number.class, "value", "decimal_field").doubleValue()).isEqualTo(100.000);
        assertThat(getNested(oneParam, Object.class, "value", "datetime_field")).isIn("2019-08-01T00:00:00", LocalDateTime.parse("2019-08-01T00:00:00"));
        assertThat(getNested(oneParam, Object.class, "value", "timestamp_field")).isIn("2019-08-02T00:00:00", LocalDateTime.parse("2019-08-02T00:00:00"));
        assertThat(getNested(oneParam, String.class, "value", "varchar_field")).isEqualTo("test string 0");

        Map<String, Object> noParam = resultMap.get("[getMySQL_paramなしのSQL]").getPersisterMap("mysql/getMySQL#paramなしのSQLpersister");
        assertThat(getNested(noParam, String.class, "value", "value_string")).isEqualTo("no param test string");

        Map<String, Object> parameterizedVariant = resultMap.get("[getMySQL_パラメタのあるvariant]").getPersisterMap("mysql/getMySQL#パラメタのあるvariantpersister");
        assertThat(getNested(parameterizedVariant, String.class, "value", "value_string")).isEqualTo("test string 6");

        Map<String, Object> branch20CacheOn = branch20.getPersisterMap("mysql/getMySQL#キャッシュONpersister");
        String branch20CacheOnValue = getNested(branch20CacheOn, String.class, "value", "value_string");
        Map<String, Object> branch60CacheOn = branch60.getPersisterMap("mysql/getMySQL#キャッシュONpersister");
        String branch60CacheOnValue = getNested(branch60CacheOn, String.class, "value", "value_string");
        Map<String, Object> branch20CacheOff = branch20.getPersisterMap("mysql/getMySQL#キャッシュOFFpersister");
        String branch20CacheOffValue = getNested(branch20CacheOff, String.class, "value", "value_string");
        Map<String, Object> branch60CacheOff = branch60.getPersisterMap("mysql/getMySQL#キャッシュOFFpersister");
        String branch60CacheOffValue = getNested(branch60CacheOff, String.class, "value", "value_string");
        assertThat(branch20CacheOnValue).isEqualTo(branch60CacheOnValue);
        assertThat(branch20CacheOffValue).isNotEqualTo(branch60CacheOffValue);
    }

    private Event createGetMySQLEvent(String testId) {
        Event result = new Event();

        result.setNamespace("mysql/getMySQL");
        result.setEventName("getMySQLイベント");
        result.setCreateTime(LocalDateTime.now());

        Map<String, Object> propertyMap = Maps.newHashMap();
        propertyMap.put("test_id", testId);
        result.setProperty(propertyMap);

        return result;
    }

    private Event createGetMySQLEventWithAttributes(String testId, int value, String persistId) {
        Event result = new Event();

        result.setNamespace("mysql/getMySQL");
        result.setEventName("getMySQLイベント属性あり");
        result.setCreateTime(LocalDateTime.now());

        Map<String, Object> propertyMap = Maps.newHashMap();
        propertyMap.put("test_id", testId);
        propertyMap.put("value", value);
        propertyMap.put("persist_id", persistId);
        result.setProperty(propertyMap);

        return result;
    }

    @Test
    void testTimezone() throws Exception {
        LOG.info("testTimezone データ送信");
        extractor.publishEvent("timezone/現在時刻を永続化", "現在時刻イベント", null, null);

        LOG.info("testTimezone 待機開始");
        Thread.sleep(40000);
        LOG.info("testTimezone 待機終了");

        Map<String, Object> persisterMap = extractor.getIdMap("[timezone]").getPersisterMap("timezone/現在時刻を永続化#現在時刻永続化");
        Object datetimeObject = getNested(persisterMap, Object.class, "value", "時刻");
        // datetimeは、DatastoreだとString, MemorystoreだとLocalDateTimeで取得される
        LocalDateTime datetime = datetimeObject instanceof LocalDateTime ?
                ((LocalDateTime) datetimeObject) :
                LocalDateTime.parse(datetimeObject.toString());

        // プロパティファイルで指定したタイムゾーンで現在時刻を取り、
        // 取得した時刻と大体同じであることを確認する
        ZoneId zoneId = ZoneId.of(timezoneId);
        LocalDateTime now = LocalDateTime.now(zoneId);
        assertThat(datetime).isBetween(now.minusMinutes(5), now);

        // さらに、UTCとの差が正しいことも確認しておく。
        ZoneOffset offset = zoneId.getRules().getOffset(Instant.EPOCH);
        int diffUTC = offset.get(ChronoField.OFFSET_SECONDS);
        LocalDateTime nowUTC = LocalDateTime.now(ZoneId.of("UTC"));
        assertThat(datetime.minusSeconds(diffUTC)).isBetween(nowUTC.minusMinutes(5), nowUTC);
    }

    @AfterAll
    static void after() throws Exception {
        Utils.withTestDb(testDbConnection -> {
            PreparedStatement deleteTestRecords = testDbConnection.prepareStatement(
                    "DELETE FROM getmysql_test WHERE test_id = ?"
            );
            deleteTestRecords.setString(1, testIdForMySQL);
            deleteTestRecords.execute();
        });
    }
}
