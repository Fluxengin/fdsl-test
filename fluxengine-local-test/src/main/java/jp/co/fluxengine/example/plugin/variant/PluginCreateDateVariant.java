package jp.co.fluxengine.example.plugin.variant;

import java.time.LocalDate;

import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("メソッドにアノテーション忘れ#createDate")
public class PluginCreateDateVariant {

	public LocalDate get() {
		try {
			return LocalDate.of(2019, 2, 28);
		} catch (Exception e) {
			return null;
		}
	}

}
