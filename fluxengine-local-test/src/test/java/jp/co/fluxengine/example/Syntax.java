package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;
import jp.co.fluxengine.stateengine.exceptions.DslParserException;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl/junit/01_パーサ/04_構文")
public class Syntax {

	@Test
	void colon() {
		assertThatThrownBy(() -> {
			testDsl("dsl/junit/01_パーサ/04_構文/コロンの付け忘れ");
		});
	}

	@Test
	void typoOfReservedWord() {
		assertThatThrownBy(() -> {
			testDsl("dsl/junit/01_パーサ/04_構文/予約語タイプミス");
		}).isInstanceOf(DslParserException.class).hasStackTraceContaining("strig");
	}

	@Test
	void inspectWithoutColon() {
		assertThatThrownBy(() -> {
			testDsl("dsl/junit/01_パーサ/04_構文/inspectにコロンの付け忘れ");
		}).isInstanceOf(DslParserException.class).hasMessageContaining("s1")
				.hasMessageContaining("\"string_imported\"")
				.hasMessageContaining("\":\" を付けてください");
	}

	@Test
	@DslPath("クオート有り無し")
	void quote(String dslPath) {
		assertThat(testDslAndGetResults(dslPath))
				.hasSize(1)
				.allMatch(TestResult::isSucceeded);
	}

	@Nested
	@DslPath("export_import")
	class ExportImport {

		@Test
		void importWithoutExport() {
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/04_構文/export_import/exportしていないものをimport");
			}).isInstanceOf(DslParserException.class).hasMessageContaining("n1");
		}

		@Test
		void fileNotfound() {
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/04_構文/export_import/import対象のファイルが存在しない");
			}).isInstanceOf(DslParserException.class).hasMessageContaining("s")
					.hasMessageContaining("「notfound.dsl」が見つかりませんでした");
		}

		@Test
		@DslPath("同じものを重複してimport")
		void importDuplication(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasMessageContaining("import s: export")
					.hasMessageContaining("duplicate");
		}

		@Test
		void aliasDuplication() {
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/04_構文/export_import/別名が重複");
			}).isInstanceOf(DslParserException.class).hasMessageContaining("バリアント");
		}
	}

	@Nested
	@DslPath("state")
	class State {

		@Test
		void withoutPersist() {
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/04_構文/state/persistがない");
			}).isInstanceOf(DslParserException.class).hasMessageContaining("persist");
		}

		@Test
		@DslPath("状態がない")
		void missingState(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasMessageContaining("「state s1」に状態が定義されていません。");
		}
	}

	@Nested
	class Persister {

		@Test
		void unnecessaryWatch() {
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/04_構文/persister/watchがある");
			}).isInstanceOf(DslParserException.class).hasStackTraceContaining("watch");
		}
	}

	@Nested
	class Rule {

		@Test
		void watchDuplicated() {
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/04_構文/rule/複数のwatch");
			}).isInstanceOf(DslParserException.class).hasStackTraceContaining("watch")
					.hasStackTraceContaining("重複");
		}
	}

	@Nested
	@DslPath("メソッド")
	class Method {

		@Test
		@DslPath("引数なしのメソッドに引数を入れる")
		void paramInNoArgMethod(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class)
					.hasStackTraceContaining("countメソッドのパラメタが設定されています。「l.count(\"dummy\")」");
		}

		@Test
		@DslPath("引数の数が少ない")
		void missingParameter(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasStackTraceContaining("「filter」関数のパラメータ未設定です。");
		}

		@Test
		@DslPath("引数の型が異なる")
		void wrongArgumentType(String dslPath) {
			// TODO 1.0.4では、パースエラーではなく実行時エラーになる
			// https://trello.com/c/Rgjr3gFm
			// 1.0.7で修正された
			//      assertThatThrownBy(() -> {
			//        testDsl(dslPath);
			//      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("endsWith")
			//          .hasStackTraceContaining("型");
		}

		@Test
		@DslPath("存在しないメソッドの呼び出し")
		void wrongMethod(String dslPath) {
			// TODO 1.0.4では、パースエラーではなく実行時エラーになる
			// https://trello.com/c/2nUlW0ka
			// 1.0.7で修正された
			//      assertThatThrownBy(() -> {
			//        testDsl(dslPath);
			//      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("contains")
			//          .hasStackTraceContaining("n1");
		}

		@Test
		@DslPath("書けない場所")
		void forbidden(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasStackTraceContaining("\"1,2,3\".split(\",\")");
		}
	}

	@Nested
	@DslPath("四則演算")
	class Arithmetic {

		@Test
		@DslPath("括弧非対応")
		void inconsistentParentheses(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasStackTraceContaining("n1")
					.hasStackTraceContaining("unbalanced braces");
		}

		@Test
		@DslPath("不明な演算子")
		void unidentifiedOperator(String dslPath) {
			// TODO 1.0.4では"%"が不明な識別子であることを特定しづらい
			// https://trello.com/c/QmcuZonn
			//      assertThatThrownBy(() -> {
			//        testDsl(dslPath);
			//      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("%")
			//          .hasStackTraceContaining("不明な識別子");
		}

		@Test
		@DslPath("書けない場所")
		void forbidden(String dslPath) {
			// TODO 1.0.4ではエラーメッセージが分かりづらい
			// https://trello.com/c/DHVKKCPT
			//      assertThatThrownBy(() -> {
			//        testDsl(dslPath);
			//      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("s1")
			//          .hasStackTraceContaining("*").hasStackTraceContaining("不明");
		}

		@Nested
		@DslPath("単項演算子")
		class UnaryOperator {
			@Test
			@DslPath("後置インクリメント")
			void postpositionIncrement(String dslPath) {
			// TODO 1.0.6ではパースエラーになる
			// https://trello.com/c/XTl3lWwS
			//	assertThat(testDslAndGetResults(dslPath))
			//			.hasSize(1)
			//			.allMatch(TestResult::isSucceeded);
			}
		}
	}

	@Nested
	@DslPath("識別子")
	class Identifier {

		@Test
		@DslPath("使用できない文字")
		void startsWithNumber(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasStackTraceContaining("1astring")
					.hasStackTraceContaining("invalid number literal");
		}

		@Test
		@DslPath("使用できない文字2")
		void includingPlus(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasStackTraceContaining("abc+123");
		}

		@Test
		@DslPath("使用できない文字3")
		void includingForbiddenCharInJava(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasMessageContaining("条件式解析異常:「━§×±.Γ〈 == true」");
		}

		@Test
		@DslPath("使用可能な文字")
		void availableCharacters(String dslPath) {
			assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
		}

		@Test
		@DslPath("予約語を識別子に使う")
		void reservedWord(String dslPath) {
			// TODO 1.0.4
			// https://trello.com/c/HeYuPeSk
			//      assertThatThrownBy(() -> {
			//        testDsl(dslPath);
			//      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("number")
			//          .hasStackTraceContaining("使えません");
		}
	}

	@Nested
	@DslPath("関数")
	class Function {

		@Test
		@DslPath("引数が少ない")
		void missingArguments(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasMessageContaining("round()")
					.hasStackTraceContaining("「round」関数のパラメータ未設定です。");
		}

		@Test
		@DslPath("引数の数が多い")
		void tooMuchArguments(String dslPath) {
			// TODO 1.0.4ではエラーメッセージが分かりづらい
			// https://trello.com/c/Z8cD928O
			// 1.0.7で修正された
			//      assertThatThrownBy(() -> {
			//        testDsl(dslPath);
			//      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("round")
			//          .hasStackTraceContaining("wrong number of arguments");
		}

		@Test
		@DslPath("引数の型が異なる")
		void wrongArgumentType(String dslPath) {
			// TODO 1.0.4ではエラーが起きず実行できてしまったが問題ないか？
			// https://trello.com/c/S7D1RA44
			// 1.0.7で修正された
			//      assertThatThrownBy(() -> {
			//        testDsl(dslPath);
			//      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("round")
			//          .hasStackTraceContaining("wrong type");
		}

		@Test
		@DslPath("関数名タイプミス")
		void typoInName(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasMessageContaining("「関数名タイプミス#n1」 詳細:「roud(1.4)」")
					.hasStackTraceContaining("「roud」存在しない関数です。");
		}

		@Test
		@DslPath("書けない場所")
		void forbidden(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class)
					.hasMessageContaining("状態名「書けない場所#ss」に使用できない文字「()」が入っています。");
		}
	}

	@Nested
	@DslPath("watch")
	class Watch {

		@Test
		@DslPath("複雑な合流")
		void complexJoin(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasMessageContaining("watch対象が存在しない");
		}
	}

	@Nested
	@DslPath("パラメタ")
	class Parameter {

		@Test
		@DslPath("日付型のパラメタ")
		void dateParameter(String dslPath) {
			assertThat(testDslAndGetResults(dslPath))
					.hasSize(2)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		@DslPath("日時型のパラメタ")
		void datetimeParameter(String dslPath) {
			assertThat(testDslAndGetResults(dslPath))
					.hasSize(2)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		@DslPath("マップ型のパラメタ")
		void mapParameter(String dslPath) {
			// TODO 1.0.5
			// https://trello.com/c/KQINzIRI
			assertThat(testDslAndGetResults(dslPath))
					.hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}
	}

	@Nested
	@DslPath("文字列結合演算子")
	class Concatenation {

		@Test
		@DslPath("文字列リテラルの結合")
		void stringLiteral(String dslPath) {
		    // TODO 1.0.6ではパースエラーになる
		    // https://trello.com/c/k3AFHLVx
			//	      assertThat(testDslAndGetResults(dslPath))
			//	          .hasSize(1)
			//	          .allMatch(TestResult::isSucceeded);
		}

		@Test
		@DslPath("文字列変数の結合")
		void stringVariable(String dslPath) {
			assertThat(testDslAndGetResults(dslPath))
					.hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		@DslPath("数字に文字列を結合")
		void numberString(String dslPath) {
			assertThat(testDslAndGetResults(dslPath))
					.hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		@DslPath("文字列に数字を結合")
		void stringNumber(String dslPath) {
			assertThat(testDslAndGetResults(dslPath))
					.hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}

	}
}
