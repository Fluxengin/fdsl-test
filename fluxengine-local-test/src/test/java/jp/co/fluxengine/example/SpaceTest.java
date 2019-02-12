package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDsl;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class SpaceTest {

	@Nested
	class RoundFunction {
		@Test
		void afterSecond() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/01_スペースの使用/関数の引数間/round第二引数の後");
			});
		}

		@Test
		void beforeSecond() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/01_スペースの使用/関数の引数間/round第二引数の前");
			});
		}
	}
	
	@Nested
	class TodayFunction {
		@Test
		void before() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/01_スペースの使用/関数の引数間/today引数の前");
			}); 
		}
	}
	
	@Nested
	class Persist {
		@Test
		void before() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/01_スペースの使用/persistの引数/引数前");
			});
		}

		@Test
		void after() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/01_スペースの使用/persistの引数/引数後");
			});
		}
	}

}
