package jp.co.fluxengine.example.dataflowtest;

import jp.co.fluxengine.example.util.PersisterExtractor;
import jp.co.fluxengine.example.util.Utils;
import okhttp3.HttpUrl;
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
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

// このクラスは、Dataflowのジョブ(バッチタイプ)にデータを投入するので、
// 普段は実行されないようにし、
// 環境変数"FLUXENGINE_INTEGRATION_TEST_MODE"が"DATAFLOWBATCH"のときだけ実行できるようにしている
// CI/CDで実行されることを想定したテストクラス
@EnabledIfEnvironmentVariable(named = "FLUXENGINE_INTEGRATION_TEST_DATAFLOW_BATCH", matches = "true|TRUE")
public class DataflowBatchTest {

    private static final Logger LOG = LoggerFactory.getLogger(DataflowBatchTest.class);

    private static PersisterExtractor extractor;

    @BeforeAll
    static void setup() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        extractor = PersisterExtractor.getInstance();
    }

    @Test
    void testRead() throws Exception {
        LOG.info("testRead 開始");
        // persisterの現在の値を取得する
        double usageBefore = extractor.currentPacketUsage("batch", "rule/日別データ検証#日別積算データ");

        // バッチにデータを流すために、バッチ用サーブレットにデータを入力する
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        HttpUrl url = HttpUrl.parse("https://" + extractor.getProjectId() + ".appspot.com/fluxengine-dataflow-batch").newBuilder()
                .addQueryParameter("event", "rule/日別データ検証#日別パケットイベント")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();

        String responseBody = response.body().string();
        LOG.debug("Response from servlet: " + responseBody);
        assertThat(response.isSuccessful()).isTrue();

        LOG.info("testRead 待機開始");
        String jobId = Utils.getJobId(responseBody);
        if (jobId == null) {
            Thread.sleep(120000);
        } else {
            Utils.waitForBatchTermination(jobId, 120000);
        }
        LOG.info("testRead 待機終了");

        // ジョブ実行後の状態のassertionを行う
        double usageAfter = extractor.currentPacketUsage("batch", "rule/日別データ検証#日別積算データ");
        assertThat(usageAfter).isEqualTo(usageBefore + 1100);
        LOG.info("testRead 終了");
    }
}
