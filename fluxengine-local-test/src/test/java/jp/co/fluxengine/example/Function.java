package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDsl;
import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;
import jp.co.fluxengine.stateengine.exceptions.DslParserException;
import org.junit.jupiter.api.Nested;
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
    assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
  }

  @Test
  @DslPath("リスト型の返り値_エラー")
  void listResultError(String dslPath) {
    // TODO 1.0.4ではパースエラーになる
    // https://trello.com/c/U2cWGek3
    assertThatThrownBy(() -> testDsl(dslPath))
            .isInstanceOf(DslParserException.class)
            .hasMessageContaining("「l1.filter(name.startsWith(\"a\"))」が解析できないため、「リスト型の返り値_エラー#l2」が定まりません");
  }

  @Nested
  @DslPath("関数の返り値に関数適用")
  class multipleApply {

    @Test
    @DslPath("プラグイン")
    void plugin(String dslPath) {
      assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("組み込み関数")
    void builtInFunction(String dslPath) {
      assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }
  }

  @Test
  @DslPath("組み込み関数")
  void builtInFunctions(String dslPath) {
    assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
  }
}
