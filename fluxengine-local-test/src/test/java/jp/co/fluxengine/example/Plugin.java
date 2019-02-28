package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDsl;
import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jp.co.fluxengine.apptest.TestResult;
import jp.co.fluxengine.stateengine.exceptions.DslParserException;

public class Plugin {

	@Test
	void wrongArgumentCount() {
		/*
		 * TODO 1.0.3 では以下のようなエラーになる
		 * jp.co.fluxengine.stateengine.exceptions.StateEngineException:
		 * メソッド呼び出し失敗:jp.co.fluxengine.example.plugin.variant.PluginSumVariant@5a5a729f
		 * メソッド名:get 引数値：[1, 2] 実行時エラーである上に、プラグインに対応するシグネチャのメソッドがないことが分かりづらい
		 */
		assertThatThrownBy(() -> {
			testDsl("dsl/junit/01_パーサ/05_プラグイン/引数の数が異なる");
		}).isInstanceOf(DslParserException.class).hasMessageContaining("sum").hasMessageContaining("get")
				.hasMessageContaining("1").hasMessageContaining("2");
	}

	@Test
	void wrongArgumentType() {
		// TODO 1.0.3では型が違うのにエラーにならない
		assertThatThrownBy(() -> {
			testDsl("dsl/junit/01_パーサ/05_プラグイン/引数の型が異なる");
		}).isInstanceOf(DslParserException.class).hasMessageContaining("concat").hasMessageContaining("get")
				.hasMessageContaining("1");
	}

	@Test
	void wrongResultType() {
		// TODO 1.0.3では型が違うのにエラーにならない
		assertThatThrownBy(() -> {
			testDsl("dsl/junit/01_パーサ/05_プラグイン/戻り値の型が異なる");
		}).isInstanceOf(DslParserException.class).hasMessageContaining("sum").hasMessageContaining("取得");
	}

	@Test
	void methodWithoutAnnotation() {
		// TODO 1.0.3では『variant解析失敗：「メソッドにアノテーション忘れ#createDate」 詳細:「get()」』というエラーメッセージで少し分かりづらい
		assertThatThrownBy(() -> {
			testDsl("dsl/junit/01_パーサ/05_プラグイン/メソッドにアノテーション忘れ");
		}).isInstanceOf(DslParserException.class).hasMessageContaining("createDate").hasMessageContaining("見つかりませんでした");
	}

}
