package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl/junit/02_エンジン/02_イベント")
public class Event {

  @Test
  @DslPath("属性無し")
  void noAttribute(String dslPath) {
    // TODO 1.0.4では、属性がないイベントを投げたはずだが、入力イベントなしと判定された
    assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
  }

  @Test
  @DslPath("複雑な属性")
  void complexAttributeStructure(String dslPath) {
    // TODO 1.0.4では、test 1でname, birthday, registration_datetimeがnullになっていた
    assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
  }

  @Test
  @DslPath("mapにenum")
  void enumInMap(String dslPath) {
    // TODO 1.0.4ではなぜかパースエラーになる
    assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
  }
}