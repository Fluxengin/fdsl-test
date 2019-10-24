package jp.co.fluxengine.example;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl/junit/03_テスト機能")
public class TestingFunction {

    @Test
    @DslPath("関数のモック化")
    void mockFunction(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("関数のモック化2")
    void mockFunction2(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("関数のモック化3")
    void mockFunction3(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("パイプライン全体のテスト")
    void testPipeline(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("例外の記録")
    void recordException(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(3).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("ログの取得")
    void matchLog(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("エフェクタの無効化")
    void invalidateEffector(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("エフェクタの無効化2")
    void invalidateEffector2(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("SQLのモック化")
    void mockSQL(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(4).allMatch(TestResult::isSucceeded);
    }
}
