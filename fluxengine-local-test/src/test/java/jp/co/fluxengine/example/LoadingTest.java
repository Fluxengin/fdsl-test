package jp.co.fluxengine.example;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static jp.co.fluxengine.apptest.TestUtils.*;

public class LoadingTest {

	@Nested
	class InFile {
		@Test
		void useBeforeDeclaration() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/02_ローディング順/ファイル内/利用箇所が先");
			});
		}
		
		@Test
		void persistBeforeEvent() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/02_ローディング順/ファイル内/persistがeventより先");
			});
		}
		
		@Test
		void crossReference() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/02_ローディング順/ファイル内/persistとvariantの相互参照");
			});
		}
		
		@Test
		void variants() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/02_ローディング順/ファイル内/2つのvariant");
			});
		}
	}
	
	@Nested
	class InterFiles {
		@Test
		void crossImport() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/02_ローディング順/ファイル間/相互にimport");
			});
		}
		
		@Test
		void variantCrossImport() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/02_ローディング順/ファイル間/variant相互参照");
			});
		}
	}
}
