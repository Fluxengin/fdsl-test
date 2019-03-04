package jp.co.fluxengine.example;

import static jp.co.fluxengine.apptest.TestUtils.testDsl;
import static jp.co.fluxengine.apptest.TestUtils.testDslAndGetResults;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jp.co.fluxengine.apptest.TestResult;
import jp.co.fluxengine.stateengine.exceptions.DslParserException;

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
		}).isInstanceOf(DslParserException.class).hasMessageContaining("s1").hasMessageContaining("\"string_imported\"")
				.hasMessageContaining("\":\" を付けてください");
	}

	@Nested
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
		void importDuplication() {
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/04_構文/export_import/同じものを重複してimport")).hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void aliasDuplication() {
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/04_構文/export_import/別名が重複");
			}).isInstanceOf(DslParserException.class).hasMessageContaining("バリアント");
		}
	}

	@Nested
	class State {
		@Test
		void withoutPersist() {
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/04_構文/state/persistがない");
			}).isInstanceOf(DslParserException.class).hasMessageContaining("persist");
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
			}).isInstanceOf(DslParserException.class).hasStackTraceContaining("watch").hasStackTraceContaining("重複");
		}
	}
}
