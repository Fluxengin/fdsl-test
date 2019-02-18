package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDsl;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TypeValidation {

	@Nested
	class Enum {
		@Test
		void testEnum() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/03_型の検証/enum");
			});
		}
	}
}
