package jp.co.fluxengine.example.dslreplacementtest;

import jp.co.fluxengine.example.util.PersisterExtractor;
import jp.co.fluxengine.example.util.Utils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

// このクラスは、Dataflowのジョブ(バッチタイプ)にデータを投入するので、
// 普段は実行されないようにし、
// 環境変数"FLUXENGINE_INTEGRATION_TEST_DSL_REPLACEMENT_BEFORE"が"true"のときだけ実行できるようにしている
// CI/CDで実行されることを想定したテストクラス
@EnabledIfEnvironmentVariable(named = "FLUXENGINE_INTEGRATION_TEST_DSL_REPLACEMENT_BEFORE", matches = "true|TRUE")
public class DslReplacementBeforeTest {

    private static final Logger LOG = LoggerFactory.getLogger(DslReplacementBeforeTest.class);

    private static PersisterExtractor extractor;

    @BeforeAll
    static void setup() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
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
        extractor.publishOneAttributeEvent("有効期限の設定", "有効期限の検証イベント", LocalDateTime.now(), "s", "before");

        // Dataflowが処理完了するまで少し待つ
        LOG.info("testPersisterLifetime 待機");
        Thread.sleep(30000);
        LOG.info("testPersisterLifetime 待機終了");

        PersisterExtractor.EntityMap entity = extractor.getIdMap("[有効期限の検証]");
        Map<String, Object> persisterMap = entity.getPersisterMap("有効期限の設定#有効期限の検証");
        assertThat(Utils.getNested(persisterMap, String.class, "value", "s")).isEqualTo("before");
        assertThat(Utils.getNested(persisterMap, String.class, "lifetime")).isEmpty();
    }

    @Test
    void testPersisterAttributes() throws Exception {
        extractor.publishOneAttributeEvent("persister項目の変更", "項目変更の検証イベント", LocalDateTime.now(), "dummy", "dummy");

        LOG.info("testPersisterAttributes 待機");
        Thread.sleep(30000);
        LOG.info("testPersisterAttributes 待機終了");

        PersisterExtractor.EntityMap entity = extractor.getIdMap("[persister項目変更の検証]");

        Map<String, Object> increasedExpired = entity.getPersisterMap("persister項目の変更#項目変更の検証_増加_期限切れ");
        assertThat(Utils.getNested(increasedExpired, String.class, "value", "s1")).isEqualTo("項目変更の検証_増加_期限切れ_before");

        Map<String, Object> decreasedExpired = entity.getPersisterMap("persister項目の変更#項目変更の検証_減少_期限切れ");
        assertThat(Utils.getNested(decreasedExpired, String.class, "value", "s2")).isEqualTo("項目変更の検証_減少_期限切れ_before");
        Number n2 = Utils.getNested(decreasedExpired, Number.class, "value", "n2");
        assertThat(n2).isNotNull().satisfies(n -> assertThat(n.intValue()).isEqualTo(2));

        Map<String, Object> increasedNotExpired = entity.getPersisterMap("persister項目の変更#項目変更の検証_増加_期限内");
        assertThat(Utils.getNested(increasedNotExpired, String.class, "value", "s3")).isEqualTo("項目変更の検証_増加_期限内_before");

        Map<String, Object> decreasedNotExpired = entity.getPersisterMap("persister項目の変更#項目変更の検証_減少_期限内");
        assertThat(Utils.getNested(decreasedNotExpired, String.class, "value", "s4")).isEqualTo("項目変更の検証_減少_期限内_before");
        Number n4 = Utils.getNested(decreasedNotExpired, Number.class, "value", "n4");
        assertThat(n4).isNotNull().satisfies(n -> assertThat(n.intValue()).isEqualTo(4));
    }

    @Test
    void testPersisterTypes() throws Exception {
        extractor.publishOneAttributeEvent("persister型変更", "型変更の検証イベント", LocalDateTime.now(), "dummy", "dummy");
        // TODO このイベントを流すと、トランザクションの不具合により、結果が上書きされてしまう
        // https://trello.com/c/8krj8lYN
//        extractor.publishOneAttributeEvent("persister型変更", "型変更の検証イベント_error1", LocalDateTime.now(), "dummy", "dummy");

        LOG.info("testPersisterTypes 待機");
        Thread.sleep(30000);
        LOG.info("testPersisterTypes 待機終了");

        PersisterExtractor.EntityMap entity = extractor.getIdMap("[persister型変更の検証]");

        Map<String, Object> expired = entity.getPersisterMap("persister型変更#型変更の検証_期限切れ");
        assertThat(Utils.getNested(expired, String.class, "value", "contents1")).isEqualTo("型変更の検証_期限切れ_before");

        String todayString = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        Map<String, Object> notExpired = entity.getPersisterMap("persister型変更#型変更の検証_期限内");
        assertThat(Utils.getNested(notExpired, String.class, "value", "contents2")).isEqualTo(todayString);

//        Map<String, Object> notExpiredError = entity.getPersisterMap("persister型変更#型変更の検証_期限内_error");
//        assertThat(Utils.getNested(notExpiredError, String.class, "value", "contents2_error")).isEqualTo(todayString);

        Map<String, Object> calculatable = entity.getPersisterMap("persister型変更#型変更の検証_計算可能1");
        assertThat(Utils.getNested(calculatable, String.class, "value", "contents3")).isEqualTo("123");
    }

    @Test
    void testPersistValues() throws Exception {
        extractor.publishOneAttributeEvent("persist値の変更", "値変更の検証イベント", LocalDateTime.now(), "input", 1);

        LOG.info("testPersistValues 待機");
        Thread.sleep(30000);
        LOG.info("testPersistValues 待機終了");

        PersisterExtractor.EntityMap entity = extractor.getIdMap("[persist値変更の検証]");

        Map<String, Object> persister = entity.getPersisterMap("persist値の変更#値変更の検証");
        assertThat(Utils.getNested(persister, Number.class, "value", "contents1"))
                .isNotNull()
                .satisfies(n -> assertThat(n.intValue()).isEqualTo(1));
    }
}
