package jp.co.fluxengine.example.dataflowtest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.fluxengine.example.util.PersisterExtractor;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

// このクラスは、Dataflowのジョブ(バッチタイプ)にデータを投入するので、
// 普段は実行されないようにし、
// 環境変数"FLUXENGINE_INTEGRATION_TEST_MODE"が"DATAFLOWBATCH"のときだけ実行できるようにしている
// CI/CDで実行されることを想定したテストクラス
@EnabledIfEnvironmentVariable(named = "FLUXENGINE_INTEGRATION_TEST_MODE", matches = "DATAFLOWBATCH|dataflowbatch")
public class DataflowBatchTest extends PersisterExtractor {

    private static final Logger LOG = LogManager.getLogger(DataflowBatchTest.class);

    @Test
    void testRead() throws Exception {
        LOG.info("testRead 開始");
        // persisterの現在の値を取得する
        double usageBefore = currentPacketUsage("uid12345", "rule/日別データ検証#日別積算データ");

        // バッチにデータを流すために、バッチ用サーブレットにデータをPOSTする
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        List<String> event = Files.readAllLines(Paths.get("input/event_batch_servlet.json"), Charset.forName("UTF-8"));
        RequestBody body = RequestBody.create(mediaType, String.join("", event));

        Request request = new Request.Builder()
                .url("https://" + projectId + ".appspot.com/fluxengine-web/event")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        String responseBody = response.body().string();
        JsonNode responseJson = new ObjectMapper().readValue(responseBody, JsonNode.class);

        assertThat(responseJson.get("status").asText()).isEqualTo("SUCCEED");

        // ここで、ジョブの実行終了を待つ
        LOG.info("testRead 待機");
        Thread.sleep(20000);
        LOG.info("testRead 待機終了");

        // ジョブ実行後の状態のassertionを行う
        double usageAfter = currentPacketUsage("uid12345", "rule/日別データ検証#日別積算データ");
        assertThat(usageAfter).isEqualTo(usageBefore + 1100);
        LOG.info("testRead 終了");
    }
}
