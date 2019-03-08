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
@DslPath("dsl/junit/01_パーサ/04_構文")
public class Syntax {

  @Test
  void colon() {
    assertThatThrownBy(() -> {
      testDsl("dsl/junit/01_パーサ/04_構文/コロンの付け忘れ");
    });
  }

  @Test
  void typoOfReservedWord() {
    assertThatThrownBy(() -> {
      testDsl("dsl/junit/01_パーサ/04_構文/予約語タイプミス");
    }).isInstanceOf(DslParserException.class).hasStackTraceContaining("strig");
  }

  @Test
  void inspectWithoutColon() {
    assertThatThrownBy(() -> {
      testDsl("dsl/junit/01_パーサ/04_構文/inspectにコロンの付け忘れ");
    }).isInstanceOf(DslParserException.class).hasMessageContaining("s1")
        .hasMessageContaining("\"string_imported\"")
        .hasMessageContaining("\":\" を付けてください");
  }

  @Nested
  class ExportImport {

    @Test
    void importWithoutExport() {
      assertThatThrownBy(() -> {
        testDsl("dsl/junit/01_パーサ/04_構文/export_import/exportしていないものをimport");
      }).isInstanceOf(DslParserException.class).hasMessageContaining("n1");
    }

    @Test
    void fileNotfound() {
      assertThatThrownBy(() -> {
        testDsl("dsl/junit/01_パーサ/04_構文/export_import/import対象のファイルが存在しない");
      }).isInstanceOf(DslParserException.class).hasMessageContaining("s")
          .hasMessageContaining("「notfound.dsl」が見つかりませんでした");
    }

    @Test
    void importDuplication() {
      // TODO 1.0.3ではエラーではなかったものが、1.0.4ではデグレードによりエラーになっていた
      // さらに、1.0.5ではimportが重複しているとエラーにするとのこと
      assertThat(testDslAndGetResults("dsl/junit/01_パーサ/04_構文/export_import/同じものを重複してimport"))
          .hasSize(1)
          .allMatch(TestResult::isSucceeded);
    }

    @Test
    void aliasDuplication() {
      assertThatThrownBy(() -> {
        testDsl("dsl/junit/01_パーサ/04_構文/export_import/別名が重複");
      }).isInstanceOf(DslParserException.class).hasMessageContaining("バリアント");
    }
  }

  @Nested
  class State {

    @Test
    void withoutPersist() {
      assertThatThrownBy(() -> {
        testDsl("dsl/junit/01_パーサ/04_構文/state/persistがない");
      }).isInstanceOf(DslParserException.class).hasMessageContaining("persist");
    }
  }

  @Nested
  class Persister {

    @Test
    void unnecessaryWatch() {
      assertThatThrownBy(() -> {
        testDsl("dsl/junit/01_パーサ/04_構文/persister/watchがある");
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("watch");
    }
  }

  @Nested
  class Rule {

    @Test
    void watchDuplicated() {
      assertThatThrownBy(() -> {
        testDsl("dsl/junit/01_パーサ/04_構文/rule/複数のwatch");
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("watch")
          .hasStackTraceContaining("重複");
    }
  }

  @Nested
  @DslPath("メソッド")
  class Method {

    @Test
    @DslPath("引数なしのメソッドに引数を入れる")
    void paramInNoArgMethod(String dslPath) {
      // TODO 1.0.4ではエラーにならない
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("dummy");
    }
  }

  @Nested
  @DslPath("四則演算")
  class Arithmetic {

    @Test
    @DslPath("括弧非対応")
    void inconsistentParentheses(String dslPath) {
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("n1")
          .hasStackTraceContaining("unbalanced braces");
    }

    @Test
    @DslPath("不明な演算子")
    void unidentifiedOperator(String dslPath) {
      // TODO 1.0.4では"%"が不明な識別子であることを特定しづらい
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("%")
          .hasStackTraceContaining("不明な識別子");
    }

    @Test
    @DslPath("書けない場所")
    void forbidden(String dslPath) {
      // TODO 1.0.4ではエラーメッセージが分かりづらい
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("s1")
          .hasStackTraceContaining("*").hasStackTraceContaining("不明");
    }
  }

  @Nested
  @DslPath("識別子")
  class Identifier {

    @Test
    @DslPath("使用できない文字")
    void startsWithNumber(String dslPath) {
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("1astring")
          .hasStackTraceContaining("invalid number literal");
    }

    @Test
    @DslPath("使用できない文字2")
    void includingPlus(String dslPath) {
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("abc+123");
    }

    @Test
    @DslPath("使用可能な文字")
    void availableCharacters(String dslPath) {
      // TODO どうやら"☆彡文字列（＾＾）"は識別子になれない？
      assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("予約語を識別子に使う")
    void reservedWord(String dslPath) {
      assertThatThrownBy(() -> {
        testDsl(dslPath);
      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("number")
          .hasStackTraceContaining("使えません");
    }
  }
}
