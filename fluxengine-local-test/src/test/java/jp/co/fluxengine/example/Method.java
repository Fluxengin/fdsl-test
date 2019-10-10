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
@DslPath("dsl/junit/02_エンジン/09_メソッド")
public class Method {

  @Nested
  @DslPath("list")
  class List {

    @Test
    @DslPath("sort")
    void sort(String dslPath) {
      assertThat(testDslAndGetResults(dslPath)).hasSize(3).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("append")
    void append(String dslPath) {
      assertThat(testDslAndGetResults(dslPath)).hasSize(3).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("sum")
    void sum(String dslPath) {
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("l1")
          .hasStackTraceContaining("sum");
    }

    @Test
    @DslPath("avg")
    void avg(String dslPath) {
      assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("contains")
    void contains(String dslPath) {
      assertThat(testDslAndGetResults(dslPath)).hasSize(3).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("join")
    void join(String dslPath) {
      assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("filter")
    void filter(String dslPath) {
      assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }
  }

  @Nested
  @DslPath("string")
  class FDSLString {

    @Test
    @DslPath("split")
    void split(String dslPath) {
      assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("format")
    void format(String dslPath) {
      assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("contains")
    void contains(String dslPath) {
      assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("startsWith")
    void startsWith(String dslPath) {
      assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("endsWith")
    void endsWith(String dslPath) {
      assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }
  }

  @Test
  @DslPath("メソッドチェーン")
  void methodChain(String dslPath) {
    assertThat(testDslAndGetResults(dslPath))
        .hasSize(2)
        .allMatch(TestResult::isSucceeded);
  }
}
