package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl/junit/02_エンジン/10_関数")
public class Function {

	@Test
	@DslPath("リスト型の引数")
	void listArgument(String dslPath) {
		assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
	}

	@Test
	@DslPath("リスト型の返り値")
	void listResult(String dslPath) {
		// TODO 1.0.4ではパースエラーになる
		// https://trello.com/c/U2cWGek3
		//    assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
	}

	@Nested
	@DslPath("関数の返り値に関数適用")
	class multipleApply {

		@Test
		@DslPath("プラグイン")
		void plugin(String dslPath) {
			assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
		}

		@Test
		@DslPath("組み込み関数")
		void builtinFunction(String dslPath) {
			assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
		}
	}

}
