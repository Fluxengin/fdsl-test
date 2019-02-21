package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.getAllResults;
import static jp.co.fluxengine.apptest.TestUtils.testDsl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jp.co.fluxengine.apptest.TestResult;
import jp.co.fluxengine.stateengine.exceptions.DslParserException;

public class TypeValidation {

	@Nested
	class Enum {
		@Test
		void numberAndString() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/03_型の検証/enum/数値と文字列");
			});
			assertThat(getAllResults("dsl/junit/01_パーサ/03_型の検証/enum/数値と文字列")).hasSize(2)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void sameName() {
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/03_型の検証/enum/同じ名称");
			}).isInstanceOf(DslParserException.class);
		}

		@Test
		void sameValue() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/03_型の検証/enum/同じ値");
			});
			assertThat(getAllResults("dsl/junit/01_パーサ/03_型の検証/enum/同じ値")).hasSize(1).allMatch(TestResult::isSucceeded);
		}
	}
}
