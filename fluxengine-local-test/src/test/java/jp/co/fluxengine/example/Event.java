package jp.co.fluxengine.example;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;
import jp.co.fluxengine.stateengine.exceptions.StateEngineException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static jp.co.fluxengine.apptest.TestUtils.testDsl;
import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl/junit/02_エンジン/02_イベント")
public class Event {

    @Test
    @DslPath("属性無し")
    void noAttribute(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("複雑な属性")
    void complexAttributeStructure(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
    }

    @Test
    @DslPath("mapにenum")
    void enumInMap(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
    }

    @Nested
    @DslPath("イベント発生日")
    class CreateTime {

        @Test
        @DslPath("取得可能")
        void successful(String dslPath) {
            assertThat(testDslAndGetResults(dslPath)).hasSize(3).allMatch(TestResult::isSucceeded);
        }

        @Test
        @DslPath("取得エラー")
        void error(String dslPath) {
            assertThatThrownBy(() -> testDsl(dslPath))
                    .isInstanceOf(StateEngineException.class)
                    .hasMessageContaining("createTime未設定");
        }
    }

    @Test
    @DslPath("順次処理")
    void sequential(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
    }

}
