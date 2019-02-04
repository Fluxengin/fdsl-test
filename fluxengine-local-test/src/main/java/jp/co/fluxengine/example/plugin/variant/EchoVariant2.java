package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("04_exportのテンプレート引数間#オウム返し2")
public class EchoVariant2 {

	@DslName("get")
	public String get(String target) {
		return target;
	}
	
}
