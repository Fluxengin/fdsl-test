package jp.co.fluxengine.example;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl/junit/02_エンジン/06_Variant")
public class Variant {

    @Test
    @DslPath("複雑な構造")
    void complexStructure(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("mapの中にenum")
    void enumInMap(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("テストからの値の設定")
    void setValueFromTest(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("キャッシュ")
    void cache(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("条件分岐")
    void conditionalBranches(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(3).allMatch(TestResult::isSucceeded);
    }
}
