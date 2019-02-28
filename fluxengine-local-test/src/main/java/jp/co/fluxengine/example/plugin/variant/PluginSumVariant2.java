package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("戻り値の型が異なる#sum")
public class PluginSumVariant2 {

	@DslName("取得")
	public String concat(int a, int b) {
		return "concat_" + Integer.toString(a) + "_" + Integer.toString(b);
	}
	
}
