package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("引数の型が異なる#concat")
public class PluginConcatVariant {

	@DslName("get")
	public String get(String prefix, int value) {
		return prefix + value;
	}
	
}
