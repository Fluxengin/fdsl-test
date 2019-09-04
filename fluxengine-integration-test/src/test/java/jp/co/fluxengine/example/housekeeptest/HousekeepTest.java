package jp.co.fluxengine.example.housekeeptest;

import jp.co.fluxengine.example.util.PersisterExtractor;
import jp.co.fluxengine.example.util.Utils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static jp.co.fluxengine.example.util.Utils.getNested;
import static org.assertj.core.api.Assertions.assertThat;

// このクラスは、Datastoreを変更するので、
// 普段は実行されないようにし、
// 環境変数"FLUXENGINE_INTEGRATION_TEST_HOUSEKEEP"が"true"のときだけ実行できるようにしている
// CI/CDで実行されることを想定したテストクラス
@EnabledIfEnvironmentVariable(named = "FLUXENGINE_INTEGRATION_TEST_HOUSEKEEP", matches = "true|TRUE")
public class HousekeepTest {

    private static final Logger LOG = LoggerFactory.getLogger(HousekeepTest.class);

    private static PersisterExtractor extractor;

    @BeforeAll
    static void setup() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        extractor = PersisterExtractor.getInstance();
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
        PersisterExtractor.IdToEntityMap before = extractor.getEntities(new String[]{
                "[user1]", "[user2]", "[user3]", "[user4]", "[user5]"
        });

        PersisterExtractor.EntityMap before1 = before.get("[user1]");
        assertThat(getNested(before1, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算A", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(before1, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算A", "lifetime")).isEmpty();
        assertThat(getNested(before1, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態A", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(before1, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態A", "lifetime")).isEmpty();
        assertThat(getNested(before1, String.class, "lifetime")).isEmpty();

        PersisterExtractor.EntityMap before2 = before.get("[user2]");
        assertThat(getNested(before2, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算B", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(before2, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算B", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(before2, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態B", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(before2, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態B", "lifetime")).isEmpty();
        assertThat(getNested(before2, String.class, "lifetime")).isEmpty();

        PersisterExtractor.EntityMap before3 = before.get("[user3]");
        assertThat(getNested(before3, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算C", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(before3, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算C", "lifetime")).isEmpty();
        assertThat(getNested(before3, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態C", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(before3, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態C", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(before3, String.class, "lifetime")).isEmpty();

        PersisterExtractor.EntityMap before4 = before.get("[user4]");
        assertThat(getNested(before4, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算D", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(before4, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算D", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(before4, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態D", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(before4, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態D", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(before4, String.class, "lifetime")).isEqualTo(todayString);

        PersisterExtractor.EntityMap before5 = before.get("[user5]");
        assertThat(getNested(before5, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算D", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(before5, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算D", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(before5, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態D", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(before5, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態D", "lifetime")).isEqualTo(tomorrowString);
        assertThat(getNested(before5, String.class, "lifetime")).isEqualTo(tomorrowString);

        // ここでHousekeepを実行する
        // 既にHousekeepのServletがデプロイされている前提
        String jobId = executeHousekeep();

        LOG.info("testAfterHousekeep 待機開始");
        if (jobId == null) {
            Thread.sleep(120000);
        } else {
            Utils.waitForBatchTermination(jobId, 120000);
        }
        LOG.info("testAfterHousekeep 待機終了");

        // Housekeep実行後の状態のassetionを行う
        PersisterExtractor.IdToEntityMap after = extractor.getEntities(new String[]{
                "[user1]", "[user2]", "[user3]", "[user4]", "[user5]"
        });

        PersisterExtractor.EntityMap after1 = after.get("[user1]");
        assertThat(getNested(after1, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算A", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(after1, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算A", "lifetime")).isEmpty();
        assertThat(getNested(after1, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態A", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(after1, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態A", "lifetime")).isEmpty();
        assertThat(getNested(after1, String.class, "lifetime")).isEmpty();

        PersisterExtractor.EntityMap after2 = after.get("[user2]");
        assertThat(getNested(after2, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算B", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(after2, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算B", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(after2, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態B", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(after2, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態B", "lifetime")).isEmpty();
        assertThat(getNested(after2, String.class, "lifetime")).isEmpty();

        PersisterExtractor.EntityMap after3 = after.get("[user3]");
        assertThat(getNested(after3, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算C", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(after3, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算C", "lifetime")).isEmpty();
        assertThat(getNested(after3, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態C", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(after3, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態C", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(after3, String.class, "lifetime")).isEmpty();

        assertThat(after).doesNotContainKeys("[user4]");

        PersisterExtractor.EntityMap after5 = after.get("[user5]");
        assertThat(getNested(after5, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算D", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(after5, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算D", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(after5, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態D", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(after5, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態D", "lifetime")).isEqualTo(tomorrowString);
        assertThat(getNested(after5, String.class, "lifetime")).isEqualTo(tomorrowString);
    }

    private static String executeHousekeep() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        String housekeepUrl = "https://" + extractor.getProjectId() + ".appspot.com/fluxengine-dataflow-housekeep";
        Request request = new Request.Builder().url(housekeepUrl).build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            LOG.info("Response from servlet: " + responseBody);
            assertThat(response.isSuccessful()).isTrue();

            return Utils.getJobId(responseBody);
        } catch (IOException e) {
            LOG.error("exception occurred", e);
            throw e;
        }
    }

}
