package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("04_exportのテンプレート引数間#加算7")
public class SumVariant7 {

	@DslName("get")
	public int get(int a, int b) {
		return a + b;
	}
	
}
