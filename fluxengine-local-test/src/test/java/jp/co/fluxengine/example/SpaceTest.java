package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDsl;
import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import jp.co.fluxengine.apptest.TestResult;
import jp.co.fluxengine.stateengine.exceptions.DslParserException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class SpaceTest {

  @Test
  void oldTests() {
    // TODO 1.0.5
    // https://trello.com/c/QPI38U1t
    assertThat(testDslAndGetResults("dsl/junit/01_パーサ/01_スペースの使用/旧方法のテストの移植")).hasSize(124)
        .allMatch(testResult -> testResult.isSucceeded());
  }

  @Nested
  class Export {

    @Test
    void beforeParethesis() {
      assertThatThrownBy(() -> {
        testDsl("dsl/junit/01_パーサ/01_スペースの使用/exportのテンプレート引数間/括弧の前にスペース");
      }).isInstanceOf(DslParserException.class).hasMessageContaining("解析失敗");
    }
  }

  @Nested
  class Import {

    @Test
    void beforeParethesis() {
      assertThatThrownBy(() -> {
        testDsl("dsl/junit/01_パーサ/01_スペースの使用/importのテンプレート引数間/括弧の前にスペース");
      }).isInstanceOf(DslParserException.class).hasMessageContaining("「オウム返し 」がexportされていません。");
    }
  }

  @Nested
  class Function {

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
  }

  @Nested
  class Method {

    @Test
    void exists() {
      // TODO 1.0.4では正しく実行できたが、1.0.5でNullPointerExceptionを起こす
      // https://trello.com/c/5Zk9F086
      assertThat(testDslAndGetResults("dsl/junit/01_パーサ/01_スペースの使用/メソッドの引数間/existsの括弧の中"))
          .hasSize(1)
          .allMatch(TestResult::isSucceeded);
    }
  }

  @Nested
  class Arithmetic {

    @Test
    void negativeWithoutParenthesis() {
      assertThatThrownBy(() -> {
        testDsl("dsl/junit/01_パーサ/01_スペースの使用/四則演算の演算子間/括弧をつけない負数");
      }).isInstanceOf(DslParserException.class).hasMessageContaining("1+-1");
    }
  }

  @Nested
  class Persist {

    @Test
    void before() {
      // TODO 1.0.4で正しく実行できていたが、1.0.5でNullPointerExceptionになってしまった。
      // https://trello.com/c/LLwRp3Bk
      assertDoesNotThrow(() -> {
        testDsl("dsl/junit/01_パーサ/01_スペースの使用/persistの引数/引数前");
      });
    }

    @Test
    void after() {
      // TODO 1.0.4で正しく実行できていたが、1.0.5でNullPointerExceptionになってしまった。
      // https://trello.com/c/LDbPSidy
      assertDoesNotThrow(() -> {
        testDsl("dsl/junit/01_パーサ/01_スペースの使用/persistの引数/引数後");
      });
    }

    @Test
    void beforeAndAfter() {
      // TODO 1.0.4で正しく実行できていたが、1.0.5でNullPointerExceptionになってしまった。
      // https://trello.com/c/lFQcRFx0
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
