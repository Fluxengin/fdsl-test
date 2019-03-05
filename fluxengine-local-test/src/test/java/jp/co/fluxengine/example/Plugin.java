package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDsl;
import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;
import jp.co.fluxengine.stateengine.exceptions.DslParserException;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl/junit/01_パーサ/05_プラグイン")
public class Plugin {

	@Test
	@DslPath("引数の数が異なる")
	void wrongArgumentCount(String dslPath) {
		/*
		 * TODO 1.0.3 では以下のようなエラーになる
		 * jp.co.fluxengine.stateengine.exceptions.StateEngineException:
		 * メソッド呼び出し失敗:jp.co.fluxengine.example.plugin.variant.PluginSumVariant@5a5a729f
		 * メソッド名:get 引数値：[1, 2] 実行時エラーである上に、プラグインに対応するシグネチャのメソッドがないことが分かりづらい
		 */
		assertThatThrownBy(() -> {
			testDsl(dslPath);
		}).isInstanceOf(DslParserException.class).hasMessageContaining("sum").hasMessageContaining("get")
				.hasMessageContaining("1").hasMessageContaining("2");
	}

	@Test
	@DslPath("引数の型が異なる")
	void wrongArgumentType(String dslPath) {
		// TODO 1.0.3では型が違うのにエラーにならない
		assertThatThrownBy(() -> {
			testDsl(dslPath);
		}).isInstanceOf(DslParserException.class).hasMessageContaining("concat").hasMessageContaining("get")
				.hasMessageContaining("1");
	}

	@Test
	@DslPath("戻り値の型が異なる")
	void wrongResultType(String dslPath) {
		// TODO 1.0.3では型が違うのにエラーにならない
		assertThatThrownBy(() -> {
			testDsl(dslPath);
		}).isInstanceOf(DslParserException.class).hasMessageContaining("sum").hasMessageContaining("取得");
	}

	@Test
	@DslPath("メソッドにアノテーション忘れ")
	void methodWithoutAnnotation(String dslPath) {
		// TODO 1.0.3では『variant解析失敗：「メソッドにアノテーション忘れ#createDate」
		// 詳細:「get()」』というエラーメッセージで少し分かりづらい
		assertThatThrownBy(() -> {
			testDsl(dslPath);
		}).isInstanceOf(DslParserException.class).hasMessageContaining("createDate")
				.hasStackTraceContaining("見つかりませんでした").hasStackTraceContaining("PluginSumVariant2");
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
