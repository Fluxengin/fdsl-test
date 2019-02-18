package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("exportのテンプレート引数間#s")
public class EchoVariant6 {

	@DslName("get")
	public String get(String target) {
		return target;
	}
	
}
