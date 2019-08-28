package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Variant("variant/ユーザー情報#数値")
public class TestNumberVariant {

	private static final Logger log = LogManager.getLogger(TestNumberVariant.class);
	private int count = 0;

	@DslName("get")
	public int get(String test) {
		count++;
		log.debug("数値 " + count + "回目 :" + test);

		return 1;
	}

}
