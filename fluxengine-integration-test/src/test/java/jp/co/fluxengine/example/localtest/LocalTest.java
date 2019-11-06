package jp.co.fluxengine.example.localtest;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl")
public class LocalTest {

    @Test
    void testAtLocal(String dslPath) {
        assertThat(testDslAndGetResults(dslPath)).hasSize(11).allMatch(TestResult::isSucceeded);
    }
}
