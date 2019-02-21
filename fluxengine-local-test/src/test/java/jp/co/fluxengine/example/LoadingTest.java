package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.getAllResults;
import static jp.co.fluxengine.apptest.TestUtils.testDsl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jp.co.fluxengine.apptest.TestResult;

public class LoadingTest {

	@Nested
	class InFile {
		@Test
		void useBeforeDeclaration() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/02_ローディング順/ファイル内/利用箇所が先");
			});
			assertThat(getAllResults("dsl/junit/01_パーサ/02_ローディング順/ファイル内/利用箇所が先")).hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void persistBeforeEvent() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/02_ローディング順/ファイル内/persistがeventより先");
			});
			assertThat(getAllResults("dsl/junit/01_パーサ/02_ローディング順/ファイル内/persistがeventより先")).hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void crossReference() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/02_ローディング順/ファイル内/persistとvariantの相互参照");
			});
			assertThat(getAllResults("dsl/junit/01_パーサ/02_ローディング順/ファイル内/persistとvariantの相互参照")).hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void variants() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/02_ローディング順/ファイル内/2つのvariant");
			});
			assertThat(getAllResults("dsl/junit/01_パーサ/02_ローディング順/ファイル内/2つのvariant")).hasSize(1)
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
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/02_ローディング順/ファイル間/相互にimport");
			});
			assertThat(getAllResults("dsl/junit/01_パーサ/02_ローディング順/ファイル間/相互にimport")).hasSize(2)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void variantCrossImport() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/02_ローディング順/ファイル間/variant相互参照");
			});
			assertThat(getAllResults("dsl/junit/01_パーサ/02_ローディング順/ファイル間/variant相互参照")).hasSize(2)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void crossImportAfterUsage() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/02_ローディング順/ファイル間/相互にimportで利用箇所が上");
			});
			assertThat(getAllResults("dsl/junit/01_パーサ/02_ローディング順/ファイル間/相互にimportで利用箇所が上")).hasSize(2)
					.allMatch(TestResult::isSucceeded);
		}
	}
}
