package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl/junit/02_エンジン/04_Persister")
public class Persister {

  @Test
  @DslPath("属性無し")
  void noAttribute(String dslPath) {
    assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
  }

  @Test
  @DslPath("複雑な属性")
  void complexAttributes(String dslPath) {
    // TODO 1.0.4では、なぜか ユーザ情報.操作履歴.count() と ユーザ情報.操作履歴.join(",") が true と評価され、しかもそれによって比較結果が true になる
    assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
  }
}
