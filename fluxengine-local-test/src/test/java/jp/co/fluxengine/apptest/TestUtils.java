package jp.co.fluxengine.apptest;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.fluxengine.stateengine.test.TestDsl;

public class TestUtils {

	private static final File mainDir;
	private static final File testDir;
	private static final File outRoot;
	private static final File logFile;
	private static final Logger LOG;

	static {
		String confPath = System.getenv("CONF");
		File confDir = new File(confPath);
		File baseDir = confDir.getParentFile();
		mainDir = new File(baseDir, "src/main");
		testDir = new File(baseDir, "src/test");
		outRoot = new File(baseDir, "out");
		logFile = new File(baseDir, "debug.log");
		LOG = LogManager.getLogger(TestUtils.class);
	}

	/*
	 * TestDsl.mainの軽いラッパー JUnitで複数のテストを同時に動かせるように、 outのフォルダをそれぞれのテストごとに作るようにしている
	 */
	public static void testDsl(String dslPath) throws Exception {
		File outDir = new File(outRoot, dslPath);
		outDir.mkdirs();
		LOG.info("テストを開始します: " + dslPath);
		TestDsl.main(new String[] { new File(mainDir, dslPath).getAbsolutePath(),
				new File(testDir, dslPath).getAbsolutePath(), outDir.getAbsolutePath(), logFile.getAbsolutePath() });
	}

	/*
	 * ここから下は、上のtestDslで実行した結果のtest-result.jsonを 簡単に参照できるようにしたメソッド群
	 * パースエラーがなく実行できた場合は、 DSLのテストが成功したかどうかも見たいはず
	 */
	public static List<TestResult> testDslAndGetResults(String dslPath) {
		try {
			testDsl(dslPath);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return getAllResults(dslPath);
	}

	public static List<TestResult> getAllResults(String folderPath) {
		return withResultStream(folderPath, testResultStream -> testResultStream.collect(Collectors.toList()));
	}

	public static Optional<TestResult> findResult(String folderPath, String testFileName, String testNo) {
		return withResultStream(folderPath,
				testResultStream -> testResultStream
						.filter(testResult -> testResult.getTestFileName().equals(testFileName)
								&& testResult.getTestNo().equals(testNo))
						.findFirst());
	}

	public static <R> R withResultStream(String folderPath, Function<Stream<TestResult>, R> body) {
		try (Stream<String> stream = Files.lines(outRoot.toPath().resolve(folderPath).resolve("test-result.json"),
				Charset.forName("UTF-8"))) {
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
}
