package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDsl;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.stateengine.exceptions.DslParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl/junit/01_パーサ/05_プラグイン")
public class Plugin {

  @Test
  @DslPath("引数の数が異なる")
  void wrongArgumentCount(String dslPath) {
    assertThatThrownBy(() -> {
      testDsl(dslPath);
    }).isInstanceOf(DslParserException.class).hasMessageContaining("プラグイン")
        .hasMessageContaining("パラメタ")
        .hasMessageContaining("DSL定義").hasMessageContaining("個数").hasMessageContaining("合わない")
        .hasMessageContaining("引数の数が異なる#sum").hasMessageContaining("定まりません");
  }

  @Test
  @DslPath("引数の型が異なる")
  void wrongArgumentType(String dslPath) {
    assertThatThrownBy(() -> {
      testDsl(dslPath);
    }).isInstanceOf(DslParserException.class).hasMessageContaining("concat")
        .hasMessageContaining("型");
  }

  @Test
  @DslPath("戻り値の型が異なる")
  void wrongResultType(String dslPath) {
    assertThatThrownBy(() -> {
      testDsl(dslPath);
    }).isInstanceOf(DslParserException.class).hasMessageContaining("sum")
        .hasMessageContaining("number").hasMessageContaining("String");
  }

  @Test
  @DslPath("メソッドにアノテーション忘れ")
  void methodWithoutAnnotation(String dslPath) {
    assertThatThrownBy(() -> {
      testDsl(dslPath);
    }).isInstanceOf(DslParserException.class).hasMessageContaining("メソッドにアノテーション忘れ#createDate")
        .hasStackTraceContaining(
            "「メソッドにアノテーション忘れ#createDate」の呼び出し「get」に対応するメソッドが「PluginCreateDateVariant」に見つかりませんでした。");
  }

  @Test
  @DslPath("クラスにアノテーション忘れ")
  void classWithoutAnnotation(String dslPath) {
    assertThatThrownBy(() -> {
      testDsl(dslPath);
    }).isInstanceOf(DslParserException.class).hasStackTraceContaining("ultimateNumber")
        .hasStackTraceContaining("見つかりませんでした");
  }
}
