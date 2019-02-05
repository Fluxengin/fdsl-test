package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("04_exportのテンプレート引数間#オウム返し5")
public class EchoVariant5 {

	@DslName("get")
	public String get(String target) {
		return target;
	}
	
}
