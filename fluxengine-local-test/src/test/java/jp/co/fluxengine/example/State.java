package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl/junit/02_エンジン/08_State")
public class State {

  @Test
  @DslPath("同じ状態への遷移")
  void selfTransition(String dslPath) {
    assertThat(testDslAndGetResults(dslPath))
        .hasSize(1)
        .allMatch(TestResult::isSucceeded);

  }
}
