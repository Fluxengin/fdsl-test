package jp.co.fluxengine.example.webservicetest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.fluxengine.example.util.PersisterExtractor;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

// このクラスは、Datastoreを変更するので、
// 普段は実行されないようにし、
// 環境変数"FLUXENGINE_INTEGRATION_TEST_MODE"が"WEBSERVICE"のときだけ実行できるようにしている
// CI/CDで実行されることを想定したテストクラス
@EnabledIfEnvironmentVariable(named = "FLUXENGINE_INTEGRATION_TEST_MODE", matches = "WEBSERVICE|webservice")
public class WebServiceTest {

    private static final Logger LOG = LogManager.getLogger(WebServiceTest.class);

    private static PersisterExtractor extractor;

    @BeforeAll
    static void setup() throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        extractor = PersisterExtractor.getInstance();
    }

    @Test
    void testWebService() throws Exception {
        LOG.info("testWebService 開始");
        // persisterの現在の値を取得する
        double usageBefore = extractor.currentPacketUsage("webservice", "webservice/パケット積算#Webserviceパケット積算データ");

        // WebServiceにデータを入力する
        String jsonString = "[{\"eventName\":\"Webserviceパケットイベント\",\"namespace\":\"webservice/パケット積算\",\"createTime\":null,\"property\":{\"使用量\":1000}}]";

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), jsonString);
        Request request = new Request.Builder()
                .url("https://" + extractor.getProjectId() + ".appspot.com/fluxengine-web/event")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        JsonNode responseJson = new ObjectMapper().readValue(response.body().string(), JsonNode.class);
        // POSTが成功したことを確認する
        assertThat(responseJson.get("status").asText()).isEqualTo("SUCCEED");

        // 処理完了まで待つ
        Thread.sleep(20000);

        // 実行後のパケット積算量を取得する
        double usageAfter = extractor.currentPacketUsage("webservice", "webservice/パケット積算#Webserviceパケット積算データ");
        assertThat(usageAfter).isEqualTo(usageBefore + 1000);
    }
}
