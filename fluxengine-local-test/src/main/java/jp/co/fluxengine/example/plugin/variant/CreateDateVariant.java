package jp.co.fluxengine.example.plugin.variant;

import java.time.LocalDate;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("プラグインからの値の受け取り#createDate")
public class CreateDateVariant {

	@DslName("get")
	public LocalDate get() {
		return LocalDate.of(2019, 2, 28);
	}

}
