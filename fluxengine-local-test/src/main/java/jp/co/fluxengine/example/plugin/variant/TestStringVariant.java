package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Variant("variant/ユーザー情報#文字列")
public class TestStringVariant {

	private static final Logger log = LogManager.getLogger(TestStringVariant.class);
	private int count = 0;

	@DslName("get")
	public String get(String test) {
		count++;
		log.debug("文字列 " + count + "回目 :" + test);

		return "あ";
	}

}
