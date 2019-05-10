package jp.co.fluxengine.example.apptest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import jp.co.fluxengine.stateengine.test.TestDsl;
import org.junit.jupiter.api.Test;

public class DslTest {

  @Test
  void test() {
    String confPath = System.getenv("CONF");
    File confDir = new File(confPath);
    File baseDir = confDir.getParentFile();
    File mainDslDir = new File(baseDir, "src/main/dsl");
    File testDslDir = new File(baseDir, "src/test/dsl");
    File outDir = new File(baseDir, "out");
    File logFile = new File(baseDir, "debug.log");

    if (!outDir.exists()) {
      outDir.mkdirs();
    }

    assertDoesNotThrow(() -> TestDsl.main(new String[]{
        mainDslDir.getAbsolutePath(),
        testDslDir.getAbsolutePath(),
        outDir.getAbsolutePath(),
        logFile.getAbsolutePath()
    }));
  }

}
