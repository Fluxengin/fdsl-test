package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.getLog;
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
@DslPath("dsl/junit/01_パーサ/03_型の検証")
public class TypeValidation {

  @Nested
  @DslPath("enum")
  class Enum {

    @Test
    @DslPath("数値と文字列")
    void numberAndString(String dslPath) {
      assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("同じ名称")
    void sameName(String dslPath) {
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("重複");
    }

    @Test
    @DslPath("同じ値")
    void sameValue(String dslPath) {
      assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }
  }

  @Nested
  @DslPath("export_import")
  class ExportImport {

    @Test
    @DslPath("型の省略")
    void typeOmitted(String dslPath) {
      // TODO 1.0.4ではエラーメッセージが分かりづらい
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("型が不明");
    }
  }

  @Nested
  class Bool {

    @Test
    void numberAsCondition() {
      assertThatThrownBy(() -> {
        testDsl("dsl/junit/01_パーサ/03_型の検証/bool/条件式にbool以外");
      }).isInstanceOf(DslParserException.class).hasMessageContaining("解析失敗");
    }

    @Test
    void complexCondition() {
      assertThatThrownBy(() -> {
        testDsl("dsl/junit/01_パーサ/03_型の検証/bool/複雑な条件式");
      }).isInstanceOf(DslParserException.class).hasMessageContaining("解析失敗");
    }

    @Test
    void complexConditionInBranch() {
      // TODO 1.0.4では失敗する
      assertThat(testDslAndGetResults("dsl/junit/01_パーサ/03_型の検証/bool/条件に複雑な条件式")).hasSize(1)
          .allMatch(TestResult::isSucceeded);
    }
  }

  @Nested
  class Persister {

    @Test
    void numberInLifetime() {
      assertThat(testDslAndGetResults("dsl/junit/01_パーサ/03_型の検証/persister/lifetimeに数値")).hasSize(1)
          .allMatch(TestResult::isSucceeded);
    }
  }

  @Nested
  class Date {

    @Test
    void fromPlugin() {
      assertThat(testDslAndGetResults("dsl/junit/01_パーサ/03_型の検証/date/プラグインからの値の受け取り")).hasSize(1)
          .allMatch(TestResult::isSucceeded);
    }
  }

  @Nested
  @DslPath("persist")
  class Persist {

    @Test
    @DslPath("persisterなし")
    void missingPersister(String dslPath) {
      // TODO 1.0.4ではNullPointerExceptionになり、何を直せばよいのかわからない。
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasMessageContaining("persister")
          .hasMessageContaining("存在しません");
    }
  }

  @Nested
  @DslPath("effect")
  class Effect {

    @Test
    @DslPath("effectorなし")
    void missingEffector(String dslPath) {
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class);
    }

    @Test
    @DslPath("違うeffectorが同じ別名")
    void sameAlias(String dslPath) {
      // TODO 1.0.4ではパースエラーにならず、f1が無視される
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasMessageContaining("f1")
          .hasMessageContaining("f2").hasMessageContaining("通知");
    }
  }

  @Nested
  @DslPath("variant")
  class Variant {

    @Test
    @DslPath("宣言無しで使用")
    void missingDeclaration(String dslPath) {
      // TODO 1.0.4ではエラーメッセージに"s1"が登場しないため、直すべき箇所が分かりづらい。
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("s1")
          .hasStackTraceContaining("s2");
    }
  }

  @Nested
  @DslPath("event")
  class Event {

    @Test
    @DslPath("宣言無しで使用")
    void missingDeclaration(String dslPath) {
      // TODO 1.0.4ではエラーメッセージが分かりづらい。"evt"が登場せず、何を直すべきか分かりづらい。
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class);
      assertThat(getLog(dslPath)).anyMatch(line -> line.contains("evt") && line.contains("宣言"));
    }
  }

  @Nested
  @DslPath("state")
  class State {

    @Test
    @DslPath("状態なしで現在の状態を参照")
    void noState(String dslPath) {
      // TODO 1.0.4ではNoSuchElementExceptionになる
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("s1")
          .hasStackTraceContaining("状態");
    }
  }

  @Nested
  @DslPath("string")
  class FDSLString {

    @Test
    @DslPath("ダブルクオート忘れ")
    void missingDoubleQuote(String dslPath) {
      // TODO 1.0.4ではエラーが起こらず実行できてしまう
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("s1")
          .hasStackTraceContaining("abcdef").hasStackTraceContaining("定義されていません");
    }

    @Test
    @DslPath("ダブルクオート非対応")
    void missingEndDoubleQuote(String dslPath) {
      // TODO 1.0.4ではエラー箇所が string s1: "fluxengineabcde_abcdef と表示されるため分かりづらい
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("s1")
          .hasStackTraceContaining("\"abcdef").hasStackTraceContaining("対応が取れていません");
    }
  }
}
