package jp.co.fluxengine.apptest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import jp.co.fluxengine.stateengine.test.TestDsl;
import org.assertj.core.api.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * VM引数 ※当javaファイル実行時に、以下のVM引数を設定 -Dlog4j.configurationFile=<log4j2.xmlへのパスを設定>
 * <p>
 * 環境変数 ※当javaファイル実行時に、以下の環境変数 CONF ... propertiesファイルなどがまとめられているディレクトリを設定 例)
 * C:\\TestProject\\conf\\ ※Fluxengine設定ファイルの他に業務処理依存の設定ファイルもここに置くこと
 */
public class TestUtils {

    private static final File MAIN_DIR;
    private static final File TEST_DIR;
    private static final File OUT_ROOT;
    private static final File LOG_FILE;
    private static final Logger LOG;

    static {
        String confPath = System.getenv("CONF");
        File confDir = new File(confPath);
        File baseDir = confDir.getParentFile();
        MAIN_DIR = new File(baseDir, "src/main");
        TEST_DIR = new File(baseDir, "src/test");
        OUT_ROOT = new File(baseDir, "out");
        LOG_FILE = new File(baseDir, "debug.log");
        LOG = LoggerFactory.getLogger(TestUtils.class);
    }

    /**
     * TestDsl.mainの軽いラッパー。JUnitで複数のテストを同時に動かせるように、 outのフォルダをそれぞれのテストごとに作るようにしている
     */
    public static void testDsl(String dslPath) throws Exception {
        File outDir = new File(OUT_ROOT, dslPath);
        outDir.mkdirs();

        LOG.info("テストを開始します: " + dslPath);

        TestDsl.main(new String[]{new File(MAIN_DIR, dslPath).getAbsolutePath(),
                new File(TEST_DIR, dslPath).getAbsolutePath(), outDir.getAbsolutePath(),
                LOG_FILE.getAbsolutePath()});
    }

    /**
     * ここから下は、上のtestDslで実行した結果のtest-result.jsonを 簡単に参照できるようにしたメソッド群 パースエラーがなく実行できた場合は、
     * DSLのテストが成功したかどうかも見たいはず
     */
    public static List<TestResult> testDslAndGetResults(String dslPath) {
        try {
            testDsl(dslPath);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getAllResults(dslPath);
    }

    public static List<TestResult> getAllResults(String folderPath) {
        return withResultStream(folderPath,
                testResultStream -> testResultStream.collect(Collectors.toList()));
    }

    public static Optional<TestResult> findResult(String folderPath, String testFileName,
                                                  String testNo) {
        return withResultStream(folderPath,
                testResultStream -> testResultStream
                        .filter(testResult -> testResult.getTestFileName().equals(testFileName)
                                && testResult.getTestNo().equals(testNo))
                        .findFirst());
    }

    public static <R> R withResultStream(String folderPath, Function<Stream<TestResult>, R> body) {
        try (Stream<String> stream = Files
                .lines(OUT_ROOT.toPath().resolve(folderPath).resolve("test-result.json"),
                        StandardCharsets.UTF_8)) {
            Stream<TestResult> results = stream.map(line -> {
                ObjectMapper mapper = new ObjectMapper();

                try {
                    return mapper.readValue(line, TestResult.class);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
            return body.apply(results);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static List<String> getLog(String dslPath) {
        try {
            List<String> lines = Files.readAllLines(LOG_FILE.toPath(), StandardCharsets.UTF_8);
            for (int startIndex = 0; startIndex < lines.size(); startIndex++) {
                if (lines.get(startIndex).contains("INFO  テストを開始します: " + dslPath)) {
                    for (int endIndex = startIndex + 1; endIndex < lines.size(); endIndex++) {
                        if (lines.get(endIndex).contains("INFO  テストを開始します: ")) {
                            return lines.subList(startIndex, endIndex);
                        }
                    }
                    return lines.subList(startIndex, lines.size());
                }
            }
            return Lists.newArrayList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Condition<Throwable> hasStackTraceContaining(String contents) {
        return new Condition<Throwable>("StackTrace has \"" + contents + "\"") {

            private String stackTrace = null;

            @Override
            public boolean matches(Throwable value) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                value.printStackTrace(printWriter);
                printWriter.close();
                stackTrace = stringWriter.toString();
                return stackTrace.contains(contents);
            }

            @Override
            public String toString() {
                return stackTrace == null ?
                        super.toString() :
                        String.format("\"%s\" should be included in <%s>", contents, stackTrace);
            }
        };
    }

    public static Condition<Throwable> hasMessageContaining(String contents) {
        return new Condition<Throwable>("Message has \"" + contents + "\"") {

            private String message = null;

            @Override
            public boolean matches(Throwable value) {
                message = value.getMessage();
                return message != null && message.contains(contents);
            }

            @Override
            public String toString() {
                return message == null ?
                        super.toString() :
                        String.format("\"%s\" should be included in <%s>", contents, message);
            }
        };
    }
}
