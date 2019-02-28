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
		// TODO 1.0.3では実行できてしまう
		assertThatThrownBy(() -> {
			testDsl("dsl/junit/01_パーサ/04_構文/予約語タイプミス");
		}).isInstanceOf(DslParserException.class).hasMessageContaining("strig");
	}

	@Test
	void inspectWithoutColon() {
		// TODO 1.0.3では
		// "string_imported"が「fluxengineabcde_string_importedfluxengineabcde_」となってしまう
		assertThatThrownBy(() -> {
			testDsl("dsl/junit/01_パーサ/04_構文/inspectにコロンの付け忘れ");
		}).isInstanceOf(DslParserException.class).hasMessageContaining("s1")
				.hasMessageContaining("\"string imported\"");
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
			// TODO 1.0.3ではエラーメッセージが不自然
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/04_構文/export_import/import対象のファイルが存在しない");
			}).isInstanceOf(DslParserException.class).hasMessageContaining("s")
					.hasMessageContaining("「notfound.dsl」が存在しません");
		}

		@Test
		void importDuplication() {
			assertThat(testDslAndGetResults("dsl/junit/01_パーサ/04_構文/export_import/同じものを重複してimport")).hasSize(1)
					.allMatch(TestResult::isSucceeded);
		}

		@Test
		void aliasDuplication() {
			// TODO 1.0.3では実行できてしまう
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/04_構文/export_import/別名が重複");
			}).isInstanceOf(DslParserException.class).hasMessageContaining("バリアント");
		}
	}

	@Nested
	class State {
		@Test
		void withoutPersist() {
			// TODO 1.0.3ではNullPointerExceptionになり、何を直せばよいかわからない
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/04_構文/state/persistがない");
			}).isInstanceOf(DslParserException.class).hasMessageContaining("state");
		}
	}
	
	@Nested
	class Persister {
		@Test
		void unnecessaryWatch() {
			// TODO 1.0.3ではNullPointerExceptionになり、何を直せばよいかわからない
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/04_構文/persister/watchがある");
			}).isInstanceOf(DslParserException.class).hasMessageContaining("watch");
		}
	}
	
	@Nested
	class Rule {
		@Test
		void watchDuplicated() {
			// TODO 1.0.3ではパースが通ってしまう
			assertThatThrownBy(() -> {
				testDsl("dsl/junit/01_パーサ/04_構文/rule/複数のwatch");
			}).isInstanceOf(DslParserException.class).hasMessageContaining("watch");
		}
	}
}
