package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl/junit/02_エンジン/01_演算子")
public class Operator {

	@Nested
	@DslPath("四則演算")
	class Arithmetic {
		@Test
		@DslPath("優先順位")
		void priority(String dslPath) {
			assertThat(testDslAndGetResults(dslPath)).hasSize(4).allMatch(TestResult::isSucceeded);
		}

		@Test
		@DslPath("巨大な整数")
		void bigNumber(String dslPath) {
			assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
		}

		@Test
		@DslPath("関数との四則演算")
		void calcAfterFunction(String dslPath) {
			assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
		}

		@Test
		@DslPath("複雑な計算式")
		void complexFormula(String dslPath) {
			assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
		}

		@Test
		@DslPath("複雑な計算式2")
		void complexFormula2(String dslPath) {
			assertThat(testDslAndGetResults(dslPath)).hasSize(3).allMatch(TestResult::isSucceeded);
		}
	}
}
