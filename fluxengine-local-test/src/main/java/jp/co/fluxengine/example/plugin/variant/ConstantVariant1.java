package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("04_exportのテンプレート引数間#定数1")
public class ConstantVariant1 {

	@DslName("get")
	public String get() {
		return "定数1";
	}
	
}
