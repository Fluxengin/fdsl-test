package jp.co.fluxengine.example.housekeeptest;

import jp.co.fluxengine.example.util.PersisterExtractor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static jp.co.fluxengine.example.util.PersisterExtractor.getNested;
import static org.assertj.core.api.Assertions.assertThat;

// このクラスは、Datastoreを変更するので、
// 普段は実行されないようにし、
// 環境変数"FLUXENGINE_INTEGRATION_TEST_MODE"が"HOUSEKEEP"のときだけ実行できるようにしている
// CI/CDで実行されることを想定したテストクラス
@EnabledIfEnvironmentVariable(named = "FLUXENGINE_INTEGRATION_TEST_MODE", matches = "HOUSEKEEP|housekeep")
public class HousekeepTest {

    private static final Logger LOG = LogManager.getLogger();

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
        Map<String, Object> before = extractor.getPersisterAsMap();

        Map<String, Object> before1 = (Map<String, Object>) before.get("[user1]");
        assertThat(getNested(before1, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算A", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(before1, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算A", "lifetime")).isEmpty();
        assertThat(getNested(before1, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態A", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(before1, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態A", "lifetime")).isEmpty();
        assertThat(getNested(before1, String.class, "lifetime")).isEmpty();

        Map<String, Object> before2 = (Map<String, Object>) before.get("[user2]");
        assertThat(getNested(before2, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算B", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(before2, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算B", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(before2, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態B", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(before2, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態B", "lifetime")).isEmpty();
        assertThat(getNested(before2, String.class, "lifetime")).isEmpty();

        Map<String, Object> before3 = (Map<String, Object>) before.get("[user3]");
        assertThat(getNested(before3, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算C", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(before3, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算C", "lifetime")).isEmpty();
        assertThat(getNested(before3, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態C", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(before3, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態C", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(before3, String.class, "lifetime")).isEmpty();

        Map<String, Object> before4 = (Map<String, Object>) before.get("[user4]");
        assertThat(getNested(before4, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算D", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(before4, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算D", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(before4, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態D", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(before4, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態D", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(before4, String.class, "lifetime")).isEqualTo(todayString);

        Map<String, Object> before5 = (Map<String, Object>) before.get("[user5]");
        assertThat(getNested(before5, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算D", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(before5, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算D", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(before5, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態D", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(before5, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態D", "lifetime")).isEqualTo(tomorrowString);
        assertThat(getNested(before5, String.class, "lifetime")).isEqualTo(tomorrowString);

        // ここでHousekeepを実行する
        // 既にHousekeepのServletがデプロイされている前提
        assertThat(executeHousekeep()).isTrue();

        // Housekeep実行後の状態のassetionを行う
        Map<String, Object> after = extractor.waitAndGetPersisterAsMap(120);

        Map<String, Object> after1 = (Map<String, Object>) after.get("[user1]");
        assertThat(getNested(after1, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算A", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(after1, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算A", "lifetime")).isEmpty();
        assertThat(getNested(after1, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態A", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(after1, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態A", "lifetime")).isEmpty();
        assertThat(getNested(after1, String.class, "lifetime")).isEmpty();

        Map<String, Object> after2 = (Map<String, Object>) after.get("[user2]");
        assertThat(getNested(after2, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算B", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(after2, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算B", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(after2, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態B", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(after2, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態B", "lifetime")).isEmpty();
        assertThat(getNested(after2, String.class, "lifetime")).isEmpty();

        Map<String, Object> after3 = (Map<String, Object>) after.get("[user3]");
        assertThat(getNested(after3, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算C", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(after3, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算C", "lifetime")).isEmpty();
        assertThat(getNested(after3, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態C", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(after3, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態C", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(after3, String.class, "lifetime")).isEmpty();

        assertThat(after).doesNotContainKeys("[user4]");

        Map<String, Object> after5 = (Map<String, Object>) after.get("[user5]");
        assertThat(getNested(after5, Object.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算D", "value", "使用量")).isInstanceOf(Number.class);
        assertThat(getNested(after5, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeepパケット積算D", "lifetime")).isEqualTo(todayString);
        assertThat(getNested(after5, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態D", "value", "currentState")).isEqualTo("s2");
        assertThat(getNested(after5, String.class, "value", "housekeep/様々なlifetimeで永続化#Housekeep状態D", "lifetime")).isEqualTo(tomorrowString);
        assertThat(getNested(after5, String.class, "lifetime")).isEqualTo(tomorrowString);
    }

    private static boolean executeHousekeep() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        String housekeepUrl = "https://" + extractor.getProjectId() + ".appspot.com/fluxengine-dataflow-housekeep";
        Request request = new Request.Builder().url(housekeepUrl).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) {
                LOG.info("got no response");
            } else {
                LOG.info("got response: " + response.body().string());
            }
            return response.isSuccessful();
        } catch (IOException e) {
            LOG.error("exception occurred", e);
            throw e;
        }
    }

}
