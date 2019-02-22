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
}
