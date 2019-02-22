package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jp.co.fluxengine.apptest.TestResult;

public class LoadingTest {

	@Nested
	class InFile {
		@Test
		void useBeforeDeclaration() {
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/02_ローディング順/ファイル内/利用箇所が先")).hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void persistBeforeEvent() {
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/02_ローディング順/ファイル内/persistがeventより先")).hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void crossReference() {
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/02_ローディング順/ファイル内/persistとvariantの相互参照")).hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void variants() {
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/02_ローディング順/ファイル内/2つのvariant")).hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void ruleCrossReference() {
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/02_ローディング順/ファイル内/rule相互参照");
			}).isInstanceOf(StackOverflowError.class);
		}
	}

	@Nested
	class InterFiles {
		@Test
		void crossImport() {
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/02_ローディング順/ファイル間/相互にimport")).hasSize(2)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void variantCrossImport() {
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/02_ローディング順/ファイル間/variant相互参照")).hasSize(2)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void crossImportAfterUsage() {
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/02_ローディング順/ファイル間/相互にimportで利用箇所が上")).hasSize(2)
					.allMatch(TestResult::isSucceeded);
		}
	}
}
