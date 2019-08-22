package jp.co.fluxengine.example.util;

import jp.co.fluxengine.example.CloudSqlPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.time.Instant;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    public static String getJobId(String servletResponse) {
        if (servletResponse == null) {
            return null;
        }

        Matcher jobIdMatcher = Pattern.compile("JobId=(.+)").matcher(servletResponse);

        if (jobIdMatcher.find()) {
            return jobIdMatcher.group(1);
        } else {
            return null;
        }
    }

    public static void waitForBatchTermination(String batchJobId, int timeoutMillis) throws IOException, InterruptedException {
        Instant timeout = Instant.now().plusMillis(timeoutMillis);

        Runtime runtime = Runtime.getRuntime();
        String[] checkStateCommand = {"/bin/sh", "-c", "gcloud dataflow jobs show " + batchJobId + " --region=asia-northeast1 | grep 'state:'"};
        Pattern statePattern = Pattern.compile("state: (.+)");

        while (Instant.now().isBefore(timeout)) {
            Process process = runtime.exec(checkStateCommand);
            process.waitFor();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"))) {
                String stateLine = br.readLine();

                if (stateLine != null) {
                    Matcher stateMatcher = statePattern.matcher(stateLine);
                    if (stateMatcher.find()) {
                        String state = stateMatcher.group(1);
                        switch (state) {
                            case "Done":
                            case "Failed":
                            case "Cancelled":
                                LOG.info("バッチジョブ " + batchJobId + " の終了を確認しました: " + state);
                                return;
                            default:
                                break;
                        }
                    } else {
                        LOG.warn("ジョブのstate行が想定外のフォーマットでした: " + stateLine);
                    }
                } else {
                    // 恐らく、ジョブの準備ができていないと思われる
                    LOG.warn("ジョブのstate行が取得できませんでした");
                }
            }

            // 次にジョブの状態を取得するまで3秒待つ
            Thread.sleep(3000);
        }

        LOG.warn("バッチジョブ " + batchJobId + " は、" + timeoutMillis + "ミリ秒以内に終了を確認できませんでした");
    }

    public static <T> T getNested(Map<String, Object> map, Class<T> resultClazz, String... paths) {
        Object result = map;

        for (String path : paths) {
            if (result == null) {
                return null;
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> next = (Map<String, Object>) result;
            result = next.get(path);
        }

        return resultClazz.cast(result);
    }

    public static void withTestDb(ThrowableConsumer<Connection> testDbConsumer) throws Exception {
        try (Connection testDbConnection = CloudSqlPool.getDataSource().getConnection()) {
            testDbConsumer.accept(testDbConnection);
        }
    }

    public interface ThrowableConsumer<T> {
        void accept(T t) throws Exception;
    }
}
