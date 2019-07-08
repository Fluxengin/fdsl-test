package jp.co.fluxengine.example.plugin.function;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Function("TestArgumentsUtility")
public class TestArgumentsUtility {

	private static final Logger log = LogManager.getLogger(TestArgumentsUtility.class);

	@DslName("getByString")
	public static String getByString(String test) {
		return test;
	}

	@DslName("getByDate")
	public static Date getByDate(Date test) {
		return test;
	}

	@DslName("getByDateTime")
	public static Date getByDateTime(Date test) {
		return test;
	}

	@DslName("getByBooleanPrimitive")
	public static boolean getByBooleanPrimitive(boolean test) {
		return test;
	}

	@DslName("getByBoolean")
	public static Boolean getByBoolean(Boolean test) {
		return test;
	}

	@DslName("getByInt")
	public static int getByInt(int test) {
		return test;
	}

	@DslName("getByLongPrimitive")
	public static long getByLongPrimitive(long test) {
		return test;
	}

	@DslName("getByFloatPrimitive")
	public static float getByFloatPrimitive(float test) {
		return test;
	}

	@DslName("getByDoublePrimitive")
	public static double getByDoublePrimitive(double test) {
		return test;
	}

	@DslName("getByInteger")
	public static Integer getByInteger(Integer test) {
		return test;
	}

	@DslName("getByLong")
	public static Long getByLong(Long test) {
		return test;
	}

	@DslName("getByFloat")
	public static Float getByLong(Float test) {
		return test;
	}

	@DslName("getByDouble")
	public static Double getByDouble(Double test) {
		return test;
	}

	@DslName("getByBigInteger")
	public static BigInteger getByBigInteger(BigInteger test) {
		return test;
	}

	@DslName("getByBigDecimal")
	public static BigDecimal getByBigDecimal(BigDecimal test) {
		return test;
	}

	@DslName("getByList")
	public static List<String> getByList(List<String> test) {
		return test;
	}

	@DslName("getByBigListWithoutTemplate")
	public static List getByBigListWithoutTemplate(List test) {
		return test;
	}

	@DslName("getByMap")
	public static Map<String, String> getByMap(Map<String, String> test) {
		return test;
	}

	@DslName("getByMapWithutTemplate")
	public static Map getByMapWithutTemplate(Map test) {
		return test;
	}

	@DslName("getByDeepMap")
	public static Map getByDeepMap(Map test) {
		return test;
	}

	@DslName("getByMapList")
	public static List getByMapList(List test) {
		return test;
	}

}
