package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("引数の数が異なる#sum")
public class PluginSumVariant {

	@DslName("get")
	public int get(int a, int b, int c) {
		return a + b + c;
	}
	
}
