package jp.co.fluxengine.apptest;

import java.io.File;

import jp.co.fluxengine.stateengine.test.TestDsl;

public class TestUtils {

	private static final File mainDir;
	private static final File testDir;
	private static final File outRoot;
	private static final File logFile;
	
	static {
		String confPath = System.getenv("CONF");
		File confDir = new File(confPath);
		File baseDir = confDir.getParentFile();
		mainDir = new File(baseDir, "src/main");
		testDir = new File(baseDir, "src/test");
		outRoot = new File(baseDir, "out");
		logFile = new File(baseDir, "debug.log");
	}
	
	public static void testDsl(String dslPath) throws Exception {
		File outDir = new File(outRoot, dslPath);
		outDir.mkdirs();
		TestDsl.main(new String[] {
				new File(mainDir, dslPath).getAbsolutePath(),
				new File(testDir, dslPath).getAbsolutePath(),
				outDir.getAbsolutePath(),
				logFile.getAbsolutePath()
		});
	}
}
