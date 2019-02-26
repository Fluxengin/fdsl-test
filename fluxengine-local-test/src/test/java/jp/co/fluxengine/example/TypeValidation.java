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
			// TODO 1.0.3では失敗する
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/03_型の検証/enum/数値と文字列")).hasSize(2)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void sameName() {
			// TODO 1.0.3では失敗する
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/03_型の検証/enum/同じ名称");
			}).isInstanceOf(DslParserException.class);
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
			// TODO 1.0.3では失敗する
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/03_型の検証/export_import/型の省略");
			}).isInstanceOf(DslParserException.class);
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
			// TODO 1.0.3では失敗する
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
}
