package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import jp.co.fluxengine.apptest.DslPath;
import jp.co.fluxengine.apptest.DslPathResolver;
import jp.co.fluxengine.apptest.TestResult;
import jp.co.fluxengine.stateengine.exceptions.DslParserException;

@ExtendWith(DslPathResolver.class)
@DslPath("dsl/junit/01_パーサ/03_型の検証")
public class TypeValidation {

	@Nested
	@DslPath("enum")
	class Enum {

		@Test
		@DslPath("数値と文字列")
		void numberAndString(String dslPath) {
			assertThat(testDslAndGetResults(dslPath)).hasSize(2).allMatch(TestResult::isSucceeded);
		}

		@Test
		@DslPath("同じ名称")
		void sameName(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasStackTraceContaining("重複");
		}

		@Test
		@DslPath("同じ値")
		void sameValue(String dslPath) {
			assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
		}

		@Test
		@DslPath("混在")
		void mixed(String dslPath) {
			assertThat(testDslAndGetResults(dslPath)).hasSize(1).allMatch(TestResult::isSucceeded);
		}
	}

	@Nested
	@DslPath("export_import")
	class ExportImport {

		@Test
		@DslPath("型の省略")
		void typeOmitted(String dslPath) {

			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class)
					.is(anyOf(hasStackTraceContaining("「export n1」の型が不明です。"),
							hasStackTraceContaining("「export n2」の型が不明です。")));
		}

		private Condition<Throwable> hasStackTraceContaining(String contents) {
			return new Condition<Throwable>("StackTrace has \"" + contents + "\"") {

				private String stackTrace = null;

				@Override
				public boolean matches(Throwable value) {
					StringWriter stringWriter = new StringWriter();
					PrintWriter printWriter = new PrintWriter(stringWriter);
					value.printStackTrace(printWriter);
					printWriter.close();
					stackTrace = stringWriter.toString();
					return stackTrace.contains(contents);
				}

				@Override
				public String toString() {
					return stackTrace == null ? super.toString()
							: "\"" + contents + "\" should be included in <" + stackTrace + ">";
				}
			};
		}
	}

	@Nested
	class Bool {

		@Test
		void numberAsCondition() {
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/03_型の検証/bool/条件式にbool以外");
			}).isInstanceOf(DslParserException.class).hasMessageContaining("解析失敗");
		}

		@Test
		void complexCondition() {
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/03_型の検証/bool/複雑な条件式");
			}).isInstanceOf(DslParserException.class).hasMessageContaining("解析失敗");
		}

		@Test
		void complexConditionInBranch() {
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/03_型の検証/bool/条件に複雑な条件式")).hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}
	}

	@Nested
	class Persister {

		@Test
		void numberInLifetime() {
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/03_型の検証/persister/lifetimeに数値")).hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}
	}

	@Nested
	@DslPath("date")
	class Date {

		@Test
		void fromPlugin() {
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/03_型の検証/date/プラグインからの値の受け取り")).hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		@DslPath("日付リテラル")
		void dateLiteral(String dslPath) {

			assertThat(testDslAndGetResults(dslPath))
					.hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}
	}

	@Nested
	@DslPath("persist")
	class Persist {

		@Test
		@DslPath("persisterなし")
		void missingPersister(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasMessageContaining("persister")
					.hasMessageContaining("存在しません");
		}
	}

	@Nested
	@DslPath("effect")
	class Effect {

		@Test
		@DslPath("effectorなし")
		void missingEffector(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class);
		}

		@Test
		@DslPath("違うeffectorが同じ別名")
		void sameAlias(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class)
					.hasStackTraceContaining("同じ名前のeffectが既に存在しています。「違うeffectorが同じ別名#通知」");
		}
	}

	@Nested
	@DslPath("variant")
	class Variant {

		@Test
		@DslPath("宣言無しで使用")
		void missingDeclaration(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasStackTraceContaining("s1")
					.hasStackTraceContaining("s2");
		}

		@Test
		@DslPath("宣言無しで使用(複数)")
		void missingDeclarationMultiple(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasStackTraceContaining("s1")
					.hasStackTraceContaining("t1")
					.hasStackTraceContaining("s2");
		}
	}

	@Nested
	@DslPath("event")
	class Event {

		@Test
		@DslPath("宣言無しで使用")
		void missingDeclaration(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasStackTraceContaining("evt").hasStackTraceContaining("宣言");
			// assertThat(getLog(dslPath)).anyMatch(line -> line.contains("evt") && line.contains("宣言"));
		}
	}

	@Nested
	@DslPath("state")
	class State {

		@Test
		@DslPath("状態なしで現在の状態を参照")
		void noState(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class)
					.hasMessageContaining("「状態なしで現在の状態を参照.dsl」において、「state s1」に状態が定義されていません。");
		}
	}

	@Nested
	@DslPath("string")
	class FDSLString {

		@Test
		@DslPath("ダブルクオート忘れ")
		void missingDoubleQuote(String dslPath) {
			      assertThatThrownBy(() -> {
			        testDsl(dslPath);
			      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("s1")
			          .hasStackTraceContaining("abcdef");
		}

		@Test
		@DslPath("ダブルクオート非対応")
		void missingEndDoubleQuote(String dslPath) {
			assertThatThrownBy(() -> {
				testDsl(dslPath);
			}).isInstanceOf(DslParserException.class).hasStackTraceContaining("string s1")
					.hasStackTraceContaining("\"abcdef")
					.hasStackTraceContaining("while scanning a quoted scalar");
		}

		@Test
		@DslPath("ダブルクオート非対応（開始なし）")
		void missingStartDoubleQuote(String dslPath) {
			//TODO:1.0.7のデグレードがないか確認する
//			assertThatThrownBy(() -> {
//				testDsl(dslPath);
//			}).isInstanceOf(DslParserException.class).hasStackTraceContaining("string s1")
//					.hasStackTraceContaining("abcdef\"")
//					.hasStackTraceContaining("while scanning a quoted scalar");
		}
	}

	@Nested
	@DslPath("struct")
	class Struct {

		@Test
		@DslPath("複雑な構造")
		void complexStructure(String dslPath) {
			assertThat(testDslAndGetResults(dslPath))
					.hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}
	}

	@Nested
	@DslPath("number")
	class Number {

		@Test
		@DslPath("日付を設定")
		void dateLiteral(String dslPath) {
		      assertThatThrownBy(() -> {
			        testDsl(dslPath);
			      }).isInstanceOf(DslParserException.class).hasStackTraceContaining("n1")
			          .hasStackTraceContaining("2018/10/10");
		}
	}
}
