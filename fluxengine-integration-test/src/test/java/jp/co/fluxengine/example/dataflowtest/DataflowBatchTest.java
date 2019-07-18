package jp.co.fluxengine.example.dataflowtest;

import jp.co.fluxengine.example.util.PersisterExtractor;
import okhttp3.HttpUrl;
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
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

// このクラスは、Dataflowのジョブ(バッチタイプ)にデータを投入するので、
// 普段は実行されないようにし、
// 環境変数"FLUXENGINE_INTEGRATION_TEST_MODE"が"DATAFLOWBATCH"のときだけ実行できるようにしている
// CI/CDで実行されることを想定したテストクラス
@EnabledIfEnvironmentVariable(named = "FLUXENGINE_INTEGRATION_TEST_MODE", matches = "DATAFLOWBATCH|dataflowbatch")
public class DataflowBatchTest {

    private static final Logger LOG = LogManager.getLogger(DataflowBatchTest.class);

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

        LOG.debug("Response from servlet: " + response.body().string());
        assertThat(response.isSuccessful()).isTrue();

        // ここで、ジョブの実行終了を待つ
        LOG.info("testRead 待機");
        Thread.sleep(120000);
        LOG.info("testRead 待機終了");

        // ジョブ実行後の状態のassertionを行う
        double usageAfter = extractor.currentPacketUsage("batch", "rule/日別データ検証#日別積算データ");
        assertThat(usageAfter).isEqualTo(usageBefore + 1100);
        LOG.info("testRead 終了");
    }
}
