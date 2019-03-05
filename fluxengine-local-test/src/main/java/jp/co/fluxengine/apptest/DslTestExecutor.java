package jp.co.fluxengine.apptest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jp.co.fluxengine.stateengine.test.TestDsl;

/**
 * ローカル開発環境でFDSLをテストするサンプルです。
 *
 * ★事前準備 package.properties variantやeffectorなどのプラグインを格納するパッケージ名を記載
 *
 * VM引数 ※当javaファイル実行時に、以下のVM引数を設定 -Dlog4j.configurationFile=<log4j2.xmlへのパスを設定>
 *
 * 環境変数 ※当javaファイル実行時に、以下の環境変数 CONF ... propertiesファイルなどがまとめられているディレクトリを設定 例)
 * C:\\TestProject\\conf\\ ※Fluxengine設定ファイルの他に業務処理依存の設定ファイルもここに置くこと
 * 
 * テストはJUnitを使うようにしたため、このクラスは基本的に使わないようになった
 */
public class DslTestExecutor {

	private static final Logger log = LogManager.getLogger(DslTestExecutor.class);

	public static void main(String[] args) throws Exception {

		/*
		 * プロジェクトのベースディレクトリを設定
		 */
		String confPath = System.getenv("CONF");
		File confDir = new File(confPath);
		File baseDir = confDir.getParentFile();

		String[] contextPaths;
		if (args.length == 0) {
			contextPaths = new String[] { "/" };
		} else {
			contextPaths = args;
		}

		File testRoot = new File(baseDir, "src/test/dsl");
		DslTestExecutor executor = new DslTestExecutor(baseDir);
		Arrays.stream(contextPaths).map(contextPath -> new File(testRoot, contextPath))
				.forEach(executor::testRecursively);

		try (FileOutputStream output = new FileOutputStream(new File(baseDir, "out/test-result.json"))) {
			FileChannel outputChannel = output.getChannel();
			
			executor.getOutPaths().stream().map(path -> path.resolve("test-result.json")).map(filePath -> filePath.toFile())
			.forEach(file -> {
				try (FileInputStream input = new FileInputStream(file)) {
					input.getChannel().transferTo(0, file.length(), outputChannel);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
		}
	}

	private final Path outRoot;
	private final List<Path> outPaths;
	private final Path testDslRoot;
	private final Path mainDslRoot;
	private final Path logPath;

	public List<Path> getOutPaths() {
		return outPaths;
	}

	public DslTestExecutor(File baseDirFile) {
		Path baseDir = baseDirFile.toPath();
		this.outRoot = baseDir.resolve("out");
		this.outPaths = new ArrayList<Path>();
		this.testDslRoot = baseDir.resolve("src/test/dsl");
		this.mainDslRoot = baseDir.resolve("src/main/dsl");
		this.logPath = baseDir.resolve("debug.log");
	}

	private void testRecursively(File dir) {
		File[] children = dir.listFiles();
		// *.dslファイルが存在すれば、コンテキストの最下層(=テストルート)と判断する
		boolean dslExists = Arrays.stream(children)
				.anyMatch(file -> file.isFile() && file.getName().toLowerCase().endsWith(".dsl"));

		if (dslExists) {
			// テスト実行
			log.debug("テストを実行します: " + dir.getAbsolutePath());
			Path currentPath = dir.toPath();
			Path relativePath = testDslRoot.relativize(currentPath);
			Path outPath = outRoot.resolve(relativePath);
			outPath.toFile().mkdirs();
			try {
				TestDsl.main(new String[] { mainDslRoot.resolve(relativePath).toString(), currentPath.toString(),
						outPath.toString(), logPath.toString() });
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			outPaths.add(outPath);
		} else {
			// 再帰的にフォルダを探索
			Arrays.stream(children).filter(file -> file.isDirectory()).forEach(this::testRecursively);
		}
	}

}
