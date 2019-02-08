package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDsl;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jp.co.fluxengine.stateengine.exceptions.DslParserException;

public class SpaceTest {

	@Nested
	class RoundFunction {
		// パースエラーになるべきテスト
		@Test
		void afterSecond() {
			assertThatThrownBy(() -> {
				testDsl("dsl/01_パーサ/01_スペースの使用/関数の引数間/round第二引数の後");
			}).isInstanceOf(DslParserException.class);
		}

		// パースエラーになるべきでないテスト
		@Test
		void beforeSecond() {
			assertDoesNotThrow(() -> {
				testDsl("dsl/01_パーサ/01_スペースの使用/関数の引数間/round第二引数の前");
			});
		}
	}

}
