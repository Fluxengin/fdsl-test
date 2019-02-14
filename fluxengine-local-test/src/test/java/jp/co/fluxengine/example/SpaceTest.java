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
		
		@Test
		void beforeAndAfter() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/01_スペースの使用/persistの引数/引数前後");
			});
		}
	}
	
	@Nested
	class ComparisonOperator {
		@Test
		void equal() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/イコール");
			});
		}
		
		@Nested
		class LessThan {
			@Test
			void noSpace() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/小なり/スペースなし");
				});
			}

			@Test
			void before() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/小なり/演算子の前");
				});
			}
			
			@Test
			void after() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/小なり/演算子の後");
				});
			}
			
			@Test
			void beforeAndAfter() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/小なり/演算子の前後");
				});
			}
		}
		
		@Nested
		class GraterThan {
			@Test
			void noSpace() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/大なり/スペースなし");
				});
			}

			@Test
			void before() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/大なり/演算子の前");
				});
			}
			
			@Test
			void after() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/大なり/演算子の後");
				});
			}
			
			@Test
			void beforeAndAfter() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/大なり/演算子の前後");
				});
			}
		}
		
		@Nested
		class LessThanOrEqualTo {
			@Test
			void noSpace() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/小なりイコール/スペースなし");
				});
			}

			@Test
			void before() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/小なりイコール/演算子の前");
				});
			}
			
			@Test
			void after() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/小なりイコール/演算子の後");
				});
			}
			
			@Test
			void beforeAndAfter() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/小なりイコール/演算子の前後");
				});
			}
		}
		
		@Nested
		class GraterThanOrEqualTo {
			@Test
			void noSpace() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/大なりイコール/スペースなし");
				});
			}

			@Test
			void before() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/大なりイコール/演算子の前");
				});
			}
			
			@Test
			void after() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/大なりイコール/演算子の後");
				});
			}
			
			@Test
			void beforeAndAfter() {
				assertDoesNotThrow(() -> {
					testDsl("dsl/junit/01_パーサ/01_スペースの使用/比較演算子/大なりイコール/演算子の前後");
				});
			}
		}
	}

}
