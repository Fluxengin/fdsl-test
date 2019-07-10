package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;

@Effector("effector/BigDecimalTestEffector#valueEffector")
public class BigDecimalTestEffector {

	private static final Logger log = LogManager.getLogger(BigDecimalTestEffector.class);

	@DslName("intValue")
	private int intValue;
	@DslName("longValue")
	private long longValue;
	@DslName("floatValue")
	private float floatValue;
	@DslName("doubleValue")
	private double doubleValue;

	@DslName("intObject")
	private Integer intObject;
	@DslName("longObject")
	private Long longObject;
	@DslName("floatValue")
	private Float floatObject;
	@DslName("doubleObject")
	private Double doubleObject;

	@DslName("bigIntegerObject")
	private BigInteger bigIntegerObject;
	@DslName("bigDecimalObject")
	private BigDecimal bigDecimalObject;

	@Post
	public void post() {

		log.debug("intValue: [" + new BigDecimal(intValue) + "] longValue: [" + new BigDecimal(longValue)
				+ "] floatValue: ["
				+ new BigDecimal(floatValue) + "] doubleValue: [" + new BigDecimal(doubleValue) + "] intObject: ["
				+ new BigDecimal(intObject) + "] longObject: [" + new BigDecimal(longObject) + "] floatValue: ["
				+ new BigDecimal(floatValue) + "]  doubleObject: [" + new BigDecimal(doubleObject)
				+ "] bigIntegerObject: ["
				+ new BigDecimal(bigIntegerObject) + "] bigDecimalObject: [" + bigDecimalObject + "]");
	}

}