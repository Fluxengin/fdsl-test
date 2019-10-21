package jp.co.fluxengine.example.dslreplacementtest;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jp.co.fluxengine.example.util.FDSLMapEntry;
import jp.co.fluxengine.example.util.PersisterExtractor;
import jp.co.fluxengine.example.util.Utils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

// このクラスは、Dataflowのジョブ(バッチタイプ)にデータを投入するので、
// 普段は実行されないようにし、
// 環境変数"FLUXENGINE_INTEGRATION_TEST_DSL_REPLACEMENT_AFTER"が"true"のときだけ実行できるようにしている
// CI/CDで実行されることを想定したテストクラス
@EnabledIfEnvironmentVariable(named = "FLUXENGINE_INTEGRATION_TEST_DSL_REPLACEMENT_AFTER", matches = "true|TRUE")
public class DslReplacementAfterTest {

    private static final Logger LOG = LoggerFactory.getLogger(DslReplacementAfterTest.class);

    private static PersisterExtractor extractor;

    @BeforeAll
    static void setup() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        extractor = PersisterExtractor.getInstance();
    }

    @BeforeEach
    void logStart(TestInfo testinfo) {
        LOG.info("{} 開始", testinfo.getDisplayName());
    }

    @AfterEach
    void logEnd(TestInfo testInfo) {
        LOG.info("{} 終了", testInfo.getDisplayName());
    }

    @Test
    void testPersisterLifetime() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayString = LocalDate.now().format(formatter);

        extractor.publishOneAttributeEvent("有効期限の設定", "有効期限の検証イベント", LocalDateTime.now(), "s", "after");

        // Dataflowが処理完了するまで少し待つ
        LOG.info("testLifetime 待機");
        Thread.sleep(40000);
        LOG.info("testLifetime 待機終了");

        PersisterExtractor.EntityMap entity = extractor.getEntityOf("[有効期限の検証]");
        Map<String, Object> persisterMap = entity.getPersisterMap("有効期限の設定#有効期限の検証");
        assertThat(Utils.<String>getNested(persisterMap, "value", "s")).isEqualTo("after");
        assertThat(Utils.<String>getNested(persisterMap, "lifetime")).isEqualTo(todayString);

        // 有効開始日が明日のDSLが実行されていないことを念のために確認する
        // もし実行されていれば、以下のエンティティが存在してしまう
        PersisterExtractor.EntityMap dummy = extractor.getEntityOf("[有効期限の検証イベントの永続化_dummy]");
        assertThat(dummy).isNull();
    }

    @Test
    void testPersisterAttributes() throws Exception {
        extractor.publishOneAttributeEvent("persister項目の変更", "項目変更の検証イベント", LocalDateTime.now(), "dummy", "dummy");

        LOG.info("testPersisterAttributes 待機");
        Thread.sleep(40000);
        LOG.info("testPersisterAttributes 待機終了");

        PersisterExtractor.EntityMap entity = extractor.getEntityOf("[persister項目変更の検証]");

        Map<String, Object> increasedExpired = entity.getPersisterMap("persister項目の変更#項目変更の検証_増加_期限切れ");
        assertThat(Utils.<String>getNested(increasedExpired, "value", "s1")).isEqualTo("_after");
        assertThat(Utils.<Number>getNested(increasedExpired, "value", "n1")).isNotNull().satisfies(n -> assertThat(n.intValue()).isEqualTo(1));

        Map<String, Object> decreasedExpired = entity.getPersisterMap("persister項目の変更#項目変更の検証_減少_期限切れ");
        assertThat(Utils.<String>getNested(decreasedExpired, "value", "s2")).isEqualTo("_after");
        @SuppressWarnings("unchecked")
        Map<String, Object> s2Map = (Map<String, Object>) decreasedExpired.get("value");
        assertThat(s2Map).doesNotContainKeys("n2");

        Map<String, Object> increasedNotExpired = entity.getPersisterMap("persister項目の変更#項目変更の検証_増加_期限内");
        assertThat(Utils.<String>getNested(increasedNotExpired, "value", "s3")).isEqualTo("項目変更の検証_増加_期限内_before_after");
        assertThat(Utils.<Number>getNested(increasedNotExpired, "value", "n3")).isNotNull().satisfies(n -> assertThat(n.intValue()).isEqualTo(3));

        Map<String, Object> decreasedNotExpired = entity.getPersisterMap("persister項目の変更#項目変更の検証_減少_期限内");
        assertThat(Utils.<String>getNested(decreasedNotExpired, "value", "s4")).isEqualTo("項目変更の検証_減少_期限内_before_after");
        @SuppressWarnings("unchecked")
        Map<String, Object> s4Map = (Map<String, Object>) decreasedNotExpired.get("value");
        assertThat(s4Map).doesNotContainKey("n4");

        // 有効開始日が明日のDSLが実行されていないことを念のために確認する
        // もし実行されていれば、以下のエンティティが存在してしまう
        PersisterExtractor.EntityMap dummy = extractor.getEntityOf("[項目変更の検証イベントの永続化_dummy]");
        assertThat(dummy).isNull();
    }

    @Test
    void testPersisterTypes() throws Exception {
        extractor.publishOneAttributeEvent("persister型変更", "型変更の検証イベント", LocalDateTime.now(), "dummy", "dummy");
        extractor.publishOneAttributeEvent("persister型変更", "型変更の検証イベント2", LocalDateTime.now(), "dummy", "dummy");
        extractor.publishOneAttributeEvent("persister型変更", "型変更の検証イベント2_error1", LocalDateTime.now(), "dummy", "dummy");

        LOG.info("testPersisterTypes 待機");
        Thread.sleep(70000);
        LOG.info("testPersisterTypes 待機終了");

        PersisterExtractor.EntityMap entity = extractor.getEntityOf("[persister型変更の検証]");

        Map<String, Object> expired = entity.getPersisterMap("persister型変更#型変更の検証_期限切れ");
        assertThat(Utils.<Number>getNested(expired, "value", "contents1")).isNotNull().satisfies(n -> assertThat(n.intValue()).isEqualTo(1));

        LocalDate today = LocalDate.now();
        String todayString = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(today);
        Map<String, Object> notExpired = entity.getPersisterMap("persister型変更#型変更の検証_期限内");
        assertThat(Utils.<String>getNested(notExpired, "value", "contents2")).isEqualTo(todayString + "_after");

        // エラーとなるため、Persisterの値が更新されない
        Map<String, Object> notExpiredError = entity.getPersisterMap("persister型変更#型変更の検証_期限内_error");
        assertThat(Utils.<Object>getNested(notExpiredError, "value", "contents2_error")).isIn(todayString, today);

        Map<String, Object> calculatable = entity.getPersisterMap("persister型変更#型変更の検証_計算可能1");
        assertThat(Utils.<Number>getNested(calculatable, "value", "contents3")).isNotNull().satisfies(n -> assertThat(n.intValue()).isEqualTo(369));

        // 有効開始日が明日のDSLが実行されていないことを念のために確認する
        // もし実行されていれば、以下のエンティティが存在してしまう
        PersisterExtractor.IdToEntityMap dummy = extractor.getEntitiesOf("[型変更の検証イベントの永続化_dummy]", "[型変更の検証イベントの永続化2_dummy]");
        assertThat(dummy).isEmpty();
    }

    @Test
    void testPersistValues() throws Exception {
        extractor.publishOneAttributeEvent("persist値の変更", "値変更の検証イベント", LocalDateTime.now(), "input", 1);

        LOG.info("testPersistValues 待機");
        Thread.sleep(40000);
        LOG.info("testPersistValues 待機終了");

        PersisterExtractor.EntityMap entity = extractor.getEntityOf("[persist値変更の検証]");

        Map<String, Object> persister = entity.getPersisterMap("persist値の変更#値変更の検証");
        assertThat(Utils.<Number>getNested(persister, "value", "contents1"))
                .isNotNull()
                .satisfies(n -> assertThat(n.intValue()).isEqualTo(5));
    }

    @Test
    void testEffectorTypes() throws Exception {
        String storagePrefix = System.getenv("STORAGE_PREFIX");

        Matcher storagePrefixMatcher = Pattern.compile("gs://(.*?)/(.*)").matcher(storagePrefix);
        if (!storagePrefixMatcher.matches()) {
            fail("STORAGE_PREFIX の書式が正しくありません: " + storagePrefix);
        }

        String bucketName = storagePrefixMatcher.group(1);
        String blobPrefix = storagePrefixMatcher.group(2);

        extractor.publishOneAttributeEvent("effector型変更", "effector型変更の検証イベント", LocalDateTime.now(), "storage_prefix", storagePrefix);

        LOG.info("testEffectorTypes 待機");
        Thread.sleep(40000);
        LOG.info("testEffectorTypes 待機終了");

        Storage storage = StorageOptions.getDefaultInstance().getService();
        assertDoesNotThrow(() -> storage.get(bucketName, blobPrefix + "型変更の検証_変換可能_after.txt"));
    }

    @Test
    void testEffectValues() throws Exception {
        String storagePrefix = System.getenv("STORAGE_PREFIX");

        Matcher storagePrefixMatcher = Pattern.compile("gs://(.*?)/(.*)").matcher(storagePrefix);
        if (!storagePrefixMatcher.matches()) {
            fail("STORAGE_PREFIX の書式が正しくありません: " + storagePrefix);
        }

        String bucketName = storagePrefixMatcher.group(1);
        String blobPrefix = storagePrefixMatcher.group(2);

        extractor.publishEvent("effect値の変更", "effect値の変更イベント", LocalDateTime.now(), Utils.toMap(
                "storage_prefix", storagePrefix,
                "attr1", "value1",
                "attr2", "value2"
        ));

        LOG.info("testEffectValues 待機");
        Thread.sleep(40000);
        LOG.info("testEffectValues 待機終了");

        Storage storage = StorageOptions.getDefaultInstance().getService();
        Blob fileBlob = storage.get(bucketName, blobPrefix + "値変更の検証_after.txt");
        String value = new String(fileBlob.getContent(), StandardCharsets.UTF_8);
        assertThat(value).isEqualTo("value2");
    }

    @Test
    void testRuleCondition() throws Exception {
        extractor.publishOneAttributeEvent("rule条件の変更", "rule条件変更の検証イベント", LocalDateTime.now(), "number_value", 5);

        LOG.info("testRuleCondition 待機");
        Thread.sleep(40000);
        LOG.info("testRuleCondition 待機終了");

        PersisterExtractor.EntityMap entity = extractor.getEntityOf("[rule条件変更の検証]");
        Map<String, Object> persister = entity.getPersisterMap("rule条件の変更#rule条件変更の検証");

        assertThat(Utils.<String>getNested(persister, "value", "contents")).isEqualTo("exists");
    }

    @Test
    void testVariantChanges() throws Exception {
        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put("attr1", 1);
        propertyMap.put("attr2", 2);
        propertyMap.put("attr3", "attr3");
        propertyMap.put("attr4", "attr4");
        propertyMap.put("attr5", "attr5");
        propertyMap.put("attr6", "attr6");
        propertyMap.put("attr7", "cached");
        propertyMap.put("attr8", "cached");

        extractor.publishEvent("variant値の変更", "variant変更の検証イベント", LocalDateTime.now(), propertyMap);

        LOG.info("testVariantChanges 待機");
        Thread.sleep(40000);
        LOG.info("testVariantChanges 待機終了");

        PersisterExtractor.EntityMap entity = extractor.getEntityOf("[variant変更の検証]");
        Map<String, Object> persister = entity.getPersisterMap("variant値の変更#variant変更の検証");

        assertThat(Utils.<Number>getNested(persister, "value", "値変更の検証"))
                .isNotNull().satisfies(n -> assertThat(n.intValue()).isEqualTo(12));
        assertThat(Utils.<String>getNested(persister, "value", "プラグインへのパラメタ変更_キャッシュ無効"))
                .isEqualTo("attr4 accepted");
        assertThat(Utils.<String>getNested(persister, "value", "プラグインへのパラメタ変更_キャッシュ有効_値変更"))
                .isEqualTo("attr6 accepted");
        assertThat(Utils.<String>getNested(persister, "value", "プラグインへのパラメタ変更_キャッシュ有効_値不変"))
                .isEqualTo("cached accepted");
    }

    @Test
    void testEventChanges() throws Exception {
        // 指定していない属性があるのでエラーとなる
        extractor.publishEvent("event属性の変更", "属性の増加の検証", LocalDateTime.now(), Utils.toMap(
                "attr1", "属性の増加の検証",
                "attr2", 10
        ));
        // 存在しない属性をしてしているのでエラーとなる
        extractor.publishEvent("event属性の変更", "属性の減少の検証", LocalDateTime.now(), Utils.toMap(
                "attr1", "属性の減少の検証",
                "attr2", 20
        ));
        // attr2の型が違うためエラーとなる
        extractor.publishEvent("event属性の変更", "属性の型変更の検証", LocalDateTime.now(), Utils.toMap(
                "attr1", "属性の型変更の検証",
                "attr2", 30
        ));

        LOG.info("testEventChanges 待機");
        Thread.sleep(40000);
        LOG.info("testEventChanges 待機終了");

        PersisterExtractor.EntityMap entity = extractor.getEntityOf("[event属性の変更の検証]");

        assertThat(entity.<Map<String, Object>>getValue("event属性の変更", "event属性の増加の検証", "contents")).containsOnly(
                FDSLMapEntry.of("contents1", "属性の増加の検証"),
                FDSLMapEntry.of("contents2", 10)
        );
        assertThat(entity.<Map<String, Object>>getValue("event属性の変更", "event属性の減少の検証", "contents")).containsOnly(
                FDSLMapEntry.of("contents1", "属性の減少の検証"),
                FDSLMapEntry.of("contents2", 20)
        );
        assertThat(entity.<Map<String, Object>>getValue("event属性の変更", "event属性の型変更の検証", "contents")).containsOnly(
                FDSLMapEntry.of("contents1", "属性の型変更の検証"),
                FDSLMapEntry.of("contents2", 30)
        );
    }
}

