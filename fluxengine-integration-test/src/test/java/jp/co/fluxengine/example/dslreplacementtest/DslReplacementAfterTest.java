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
// 環境変数"FLUXENGINE_INTEGRATION_TEST_DSL_REPLACEMENT_AFTER"が"true"のときだけ実行できるようにしている
// CI/CDで実行されることを想定したテストクラス
@EnabledIfEnvironmentVariable(named = "FLUXENGINE_INTEGRATION_TEST_DSL_REPLACEMENT_AFTER", matches = "true|TRUE")
public class DslReplacementAfterTest {

    private static final Logger LOG = LoggerFactory.getLogger(DslReplacementAfterTest.class);

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayString = LocalDate.now().format(formatter);

        extractor.publishOneAttributeEvent("有効期限の設定", "有効期限の検証イベント", LocalDateTime.now(), "s", "after");

        // Dataflowが処理完了するまで少し待つ
        LOG.info("testLifetime 待機");
        Thread.sleep(30000);
        LOG.info("testLifetime 待機終了");

        PersisterExtractor.EntityMap entity = extractor.getIdMap("[有効期限の検証]");
        Map<String, Object> persisterMap = entity.getPersisterMap("有効期限の設定#有効期限の検証");
        assertThat(Utils.getNested(persisterMap, String.class, "value", "s")).isEqualTo("after");
        assertThat(Utils.getNested(persisterMap, String.class, "lifetime")).isEqualTo(todayString);

        // 有効開始日が明日のDSLが実行されていないことを念のために確認する
        // もし実行されていれば、以下のエンティティが存在してしまう
        PersisterExtractor.EntityMap dummy = extractor.getIdMap("[有効期限の検証イベントの永続化_dummy]");
        assertThat(dummy).isNull();
    }

    @Test
    void testPersisterAttributes() throws Exception {
        // testPersisterLifetimeによってDSLが最新バージョンに更新されるのを待つ
        Thread.sleep(3000);

        extractor.publishOneAttributeEvent("persister項目の変更", "項目変更の検証イベント", LocalDateTime.now(), "dummy", "dummy");

        LOG.info("testPersisterAttributes 待機");
        Thread.sleep(30000);
        LOG.info("testPersisterAttributes 待機終了");

        PersisterExtractor.EntityMap entity = extractor.getIdMap("[persister項目変更の検証]");

        Map<String, Object> increasedExpired = entity.getPersisterMap("persister項目の変更#項目変更の検証_増加_期限切れ");
        assertThat(Utils.getNested(increasedExpired, String.class, "value", "s1")).isEqualTo("_after");
        assertThat(Utils.getNested(increasedExpired, Number.class, "value", "n1")).isNotNull().satisfies(n -> assertThat(n.intValue()).isEqualTo(1));

        Map<String, Object> decreasedExpired = entity.getPersisterMap("persister項目の変更#項目変更の検証_減少_期限切れ");
        assertThat(Utils.getNested(decreasedExpired, String.class, "value", "s2")).isEqualTo("_after");
        @SuppressWarnings("unchecked")
        Map<String, Object> s2Map = (Map<String, Object>) decreasedExpired.get("value");
        assertThat(s2Map).doesNotContainKeys("n2");

        Map<String, Object> increasedNotExpired = entity.getPersisterMap("persister項目の変更#項目変更の検証_増加_期限内");
        assertThat(Utils.getNested(increasedNotExpired, String.class, "value", "s3")).isEqualTo("項目変更の検証_増加_期限内_before_after");
        assertThat(Utils.getNested(increasedNotExpired, Number.class, "value", "n3")).isNotNull().satisfies(n -> assertThat(n.intValue()).isEqualTo(3));

        Map<String, Object> decreasedNotExpired = entity.getPersisterMap("persister項目の変更#項目変更の検証_減少_期限内");
        assertThat(Utils.getNested(decreasedNotExpired, String.class, "value", "s4")).isEqualTo("項目変更の検証_減少_期限内_before_after");
        @SuppressWarnings("unchecked")
        Map<String, Object> s4Map = (Map<String, Object>) decreasedNotExpired.get("value");
        assertThat(s4Map).doesNotContainKey("n4");

        // 有効開始日が明日のDSLが実行されていないことを念のために確認する
        // もし実行されていれば、以下のエンティティが存在してしまう
        PersisterExtractor.EntityMap dummy = extractor.getIdMap("[項目変更の検証イベントの永続化_dummy]");
        assertThat(dummy).isNull();
    }

    @Test
    void testPersisterTypes() throws Exception {
        // testPersisterLifetimeによってDSLが最新バージョンに更新されるのを待つ
        Thread.sleep(3000);

        extractor.publishOneAttributeEvent("persister型変更", "型変更の検証イベント", LocalDateTime.now(), "dummy", "dummy");
        extractor.publishOneAttributeEvent("persister型変更", "型変更の検証イベント2", LocalDateTime.now(), "dummy", "dummy");

        LOG.info("testPersisterTypes 待機");
        Thread.sleep(30000);
        LOG.info("testPersisterTypes 待機終了");

        PersisterExtractor.EntityMap entity = extractor.getIdMap("[persister型変更の検証]");

        Map<String, Object> expired = entity.getPersisterMap("persister型変更#型変更の検証_期限切れ");
        assertThat(Utils.getNested(expired, Number.class, "value", "contents1")).isNotNull().satisfies(n -> assertThat(n.intValue()).isEqualTo(1));

        String todayString = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        Map<String, Object> notExpired = entity.getPersisterMap("persister型変更#型変更の検証_期限内");
        assertThat(Utils.getNested(notExpired, String.class, "value", "contents2")).isEqualTo(todayString + "_after");

        Map<String, Object> calculatable = entity.getPersisterMap("persister型変更#型変更の検証_計算可能1");
        assertThat(Utils.getNested(calculatable, Number.class, "value", "contents3")).isNotNull().satisfies(n -> assertThat(n.intValue()).isEqualTo(369));

        // 有効開始日が明日のDSLが実行されていないことを念のために確認する
        // もし実行されていれば、以下のエンティティが存在してしまう
        PersisterExtractor.IdToEntityMap dummy = extractor.getEntities("[型変更の検証イベントの永続化_dummy]", "[型変更の検証イベントの永続化2_dummy]");
        assertThat(dummy).isEmpty();
    }
}
