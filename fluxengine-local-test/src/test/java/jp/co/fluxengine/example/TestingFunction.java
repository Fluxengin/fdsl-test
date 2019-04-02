package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl/junit/03_テスト機能")
public class TestingFunction {

  @Test
  @DslPath("関数のモック化")
  void mockFunction(String dslPath) {
    assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
  }

  @Test
  @DslPath("関数のモック化2")
  void mockFunction2(String dslPath) {
    // TODO 1.0.4では"runner"がパースエラーになる
    assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
  }

  @Test
  @DslPath("パイプライン全体のテスト")
  void testPipeline(String dslPath) {
    // TODO 1.0.5では、logが出力されているのに log == で検出できないうえに、
    // 2018/11/11 00:00:00にpersister pがリセットされなかった
    assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
  }

  @Test
  @DslPath("例外の記録")
  void recordException(String dslPath) {
    assertThat(testDslAndGetResults(dslPath)).hasSize(3).allMatch(TestResult::isSucceeded);
  }

  @Test
  @DslPath("ログの取得")
  void matchLog(String dslPath) {
    assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
  }
}
