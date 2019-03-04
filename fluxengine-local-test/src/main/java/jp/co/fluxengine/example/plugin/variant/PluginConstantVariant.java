package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;

public class PluginConstantVariant {

	@DslName("get")
	public Number get() {
		return 42;
	}
	
}
