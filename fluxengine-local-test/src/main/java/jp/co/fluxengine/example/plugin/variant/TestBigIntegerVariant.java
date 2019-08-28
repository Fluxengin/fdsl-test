package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;

@Variant("variant/ユーザー情報#BigInteger")
public class TestBigIntegerVariant {

	private static final Logger log = LogManager.getLogger(TestBigIntegerVariant.class);
	private int count = 0;

	@DslName("get")
	public BigInteger get(String test) {
		count++;
		log.debug("TestBigIntegerVariant " + count + "回目 :" + test);

		return BigInteger.ONE;
	}

}
