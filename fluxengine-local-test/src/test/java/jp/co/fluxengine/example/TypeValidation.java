package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDsl;
import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jp.co.fluxengine.apptest.TestResult;
import jp.co.fluxengine.stateengine.exceptions.DslParserException;

public class TypeValidation {

	@Nested
	class Enum {
		@Test
		void numberAndString() {
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/03_型の検証/enum/数値と文字列")).hasSize(2)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void sameName() {
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/03_型の検証/enum/同じ名称");
			}).isInstanceOf(DslParserException.class).hasStackTraceContaining("重複");
		}

		@Test
		void sameValue() {
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/03_型の検証/enum/同じ値")).hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}
	}

	@Nested
	class ExportImport {
		@Test
		void typeOmitted() {
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/03_型の検証/export_import/型の省略");
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
			// TODO 比較結果がfalseになる。dateとjava.util.Dateは違うのか？
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/03_型の検証/date/プラグインからの値の受け取り")).hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}
	}

}
