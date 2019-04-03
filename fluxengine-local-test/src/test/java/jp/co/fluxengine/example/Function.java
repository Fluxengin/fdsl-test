package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl/junit/02_エンジン/10_関数")
public class Function {

  @Test
  @DslPath("リスト型の引数")
  void listArgument(String dslPath) {
    assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
  }

  @Test
  @DslPath("リスト型の返り値")
  void listResult(String dslPath) {
    // TODO 1.0.4ではパースエラーになる
    // https://trello.com/c/U2cWGek3
    assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
  }

  @Test
  @DslPath("関数の返り値に関数適用")
  void multipleApply(String dslPath) {
    // TODO 1.0.4ではパースエラーになる
    // https://trello.com/c/v1KCGocl
    assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
  }
}
