package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("04_exportのテンプレート引数間#加算6")
public class SumVariant6 {

	@DslName("get")
	public int get(int a, int b) {
		return a + b;
	}
	
}
