package jp.co.fluxengine.example;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;
import jp.co.fluxengine.stateengine.exceptions.DslParserException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static jp.co.fluxengine.apptest.TestUtils.testDsl;
import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl/junit/InternalTestProjectの移行")
public class InternalTestProject {

    @Test
    @DslPath("bugfix")
    void testBugfix(String dslPath) {
        assertThatThrownBy(() -> testDsl(dslPath))
                .isInstanceOf(DslParserException.class)
                .hasMessageContaining("file1.dslロード失敗しました。")
                .hasStackTraceContaining("while scanning a quoted scalar")
                .hasStackTraceContaining("export string s: \"string");
    }

    @Nested
    @DslPath("error")
    class Error {

        @Test
        @DslPath("20190322")
        void test20190322(String dslPath) {
            assertThatThrownBy(() -> testDsl(dslPath))
                    .isInstanceOf(DslParserException.class)
                    .hasMessageContaining("「rule/AC24PrintedAdOrCampRule#campaignName」に、string型の値は代入できません");
        }

        @Test
        @DslPath("old_201901")
        void testOld201901(String dslPath) {
            assertThatThrownBy(() -> testDsl(dslPath))
                    .isInstanceOf(DslParserException.class)
                    .hasMessageContaining("「rule/AC07Rule#result」に、list型の値は代入できません");
        }
    }

    @Nested
    @DslPath("v1.0.3")
    class V103 {

        @Test
        @DslPath("effector/normal")
        void testEffector(String dslPath) {
            assertThat(testDslAndGetResults(dslPath)).hasSize(6)
                    .contains(new TestResult("rule\\パケット積算.dsl", "test 1 通常積算", false))
                    .contains(new TestResult("rule\\パケット積算.dsl", "test 2 上限超過", false))
                    .filteredOn(testResult -> !testResult.testFileName.equals("rule\\パケット積算.dsl"))
                    .allMatch(TestResult::isSucceeded);
        }

        @Test
        @DslPath("list/normal")
        void testList(String dslPath) {
            assertThatThrownBy(() -> testDsl(dslPath))
                    .isInstanceOf(DslParserException.class)
                    .hasMessageContaining("条件式解析異常:「standard_case_6_result_4==6.0」");
        }

        @Test
        @DslPath("map/normal")
        void testMap(String dslPath) {
            assertThat(testDslAndGetResults(dslPath))
                    .hasSize(13)
                    .allMatch(TestResult::isSucceeded);
        }
    }

    @Nested
    @DslPath("v1.0.4")
    class V104 {

        @Test
        @DslPath("big_integer/normal")
        void testBigInteger(String dslPath) {
            assertThat(testDslAndGetResults(dslPath))
                    .hasSize(1)
                    .allMatch(TestResult::isSucceeded);
        }

        @Test
        @DslPath("condition/normal")
        void testCondition(String dslPath) {
            assertThat(testDslAndGetResults(dslPath))
                    .hasSize(7)
                    .allMatch(TestResult::isSucceeded);
        }

        @Test
        @DslPath("contains/normal")
        void testContains(String dslPath) {
            assertThatThrownBy(() -> testDsl(dslPath))
                    .isInstanceOf(DslParserException.class)
                    .hasMessageContaining("「「rule/パケット積算#n」の型不正、containsメソッドには、文字列またリストにしか適用しません」ため、「rule/パケット積算#b99」が定まりません。");
        }

        @Test
        @DslPath("persister/normal")
        void testPersister(String dslPath) {
            assertThat(testDslAndGetResults(dslPath))
                    .hasSize(2)
                    .allMatch(TestResult::isSucceeded);
        }

        @Test
        @DslPath("variant_cache/normal")
        void testVariantCache(String dslPath) {
            assertThat(testDslAndGetResults(dslPath))
                    .hasSize(3)
                    .allMatch(TestResult::isSucceeded);
        }
    }

    @Nested
    @DslPath("v1.0.5")
    class V105 {

        @Test
        @DslPath("arguments")
        void testArguments(String dslPath) {
            assertThat(testDslAndGetResults(dslPath))
                    .hasSize(42)
                    .allMatch(TestResult::isSucceeded);
        }

        @Test
        @DslPath("bigdecimal")
        void testBigdecimal(String dslPath) {
            assertThat(testDslAndGetResults(dslPath))
                    .hasSize(24)
                    .allMatch(TestResult::isSucceeded);
        }

        @Test
        @DslPath("calc")
        void testCalc(String dslPath) {
            assertThat(testDslAndGetResults(dslPath))
                    .hasSize(33)
                    .allMatch(TestResult::isSucceeded);
        }

        @Test
        @DslPath("date_functions")
        void testDateFunctions(String dslPath) {
            assertThat(testDslAndGetResults(dslPath))
                    .hasSize(71)
                    .allMatch(TestResult::isSucceeded);
        }

        @Test
        @DslPath("event")
        void testEvent(String dslPath) {
            assertThat(testDslAndGetResults(dslPath))
                    .hasSize(3)
                    .allMatch(TestResult::isSucceeded);
        }

        @Test
        @DslPath("quotation")
        void testQuotation(String dslPath) {
            assertThat(testDslAndGetResults(dslPath))
                    .hasSize(32)
                    .allMatch(TestResult::isSucceeded);
        }
    }
}
