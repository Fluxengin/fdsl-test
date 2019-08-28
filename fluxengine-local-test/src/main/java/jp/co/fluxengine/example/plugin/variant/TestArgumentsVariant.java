package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Variant("variant/TestArgumentsVariant#getByString,variant/TestArgumentsVariant#getByDate,"
		+ "variant/TestArgumentsVariant#getByDateTime,variant/TestArgumentsVariant#getByBooleanPrimitive,"
		+ "variant/TestArgumentsVariant#getByBoolean,variant/TestArgumentsVariant#getByInt,"
		+ "variant/TestArgumentsVariant#getByLongPrimitive,variant/TestArgumentsVariant#getByFloatPrimitive,"
		+ "variant/TestArgumentsVariant#getByDoublePrimitive,variant/TestArgumentsVariant#getByInteger,"
		+ "variant/TestArgumentsVariant#getByLong,variant/TestArgumentsVariant#getByFloat,"
		+ "variant/TestArgumentsVariant#getByDouble,variant/TestArgumentsVariant#getByBigInteger,"
		+ "variant/TestArgumentsVariant#getByBigDecimal,"
		+ "variant/TestArgumentsVariant#getByList,variant/TestArgumentsVariant#getByBigListWithoutTemplate,"
		+ "variant/TestArgumentsVariant#getByMapList,variant/TestArgumentsVariant#getByDeepMap,"
		+ "variant/TestArgumentsVariant#getByMap,variant/TestArgumentsVariant#getByMapWithutTemplate")
public class TestArgumentsVariant {

	private static final Logger log = LogManager.getLogger(TestArgumentsVariant.class);

	@DslName("getByString")
	public String getByString(String test) {
		return test;
	}

	@DslName("getByDate")
	public Date getByDate(Date test) {
		return test;
	}

	@DslName("getByDateTime")
	public Date getByDateTime(Date test) {
		return test;
	}

	@DslName("getByBooleanPrimitive")
	public boolean getByBooleanPrimitive(boolean test) {
		return test;
	}

	@DslName("getByBoolean")
	public Boolean getByBoolean(Boolean test) {
		return test;
	}

	@DslName("getByInt")
	public int getByInt(int test) {
		return test;
	}

	@DslName("getByLongPrimitive")
	public long getByLongPrimitive(long test) {
		return test;
	}

	@DslName("getByFloatPrimitive")
	public float getByFloatPrimitive(float test) {
		return test;
	}

	@DslName("getByDoublePrimitive")
	public double getByDoublePrimitive(double test) {
		return test;
	}

	@DslName("getByInteger")
	public Integer getByInteger(Integer test) {
		return test;
	}

	@DslName("getByLong")
	public Long getByLong(Long test) {
		return test;
	}

	@DslName("getByFloat")
	public Float getByLong(Float test) {
		return test;
	}

	@DslName("getByDouble")
	public Double getByDouble(Double test) {
		return test;
	}

	@DslName("getByBigInteger")
	public BigInteger getByBigInteger(BigInteger test) {
		return test;
	}

	@DslName("getByBigDecimal")
	public BigDecimal getByBigDecimal(BigDecimal test) {
		return test;
	}

	@DslName("getByList")
	public List<String> getByList(List<String> test) {
		return test;
	}

	@DslName("getByBigListWithoutTemplate")
	public List getByBigListWithoutTemplate(List test) {
		return test;
	}

	@DslName("getByMap")
	public Map<String, String> getByMap(Map<String, String> test) {
		return test;
	}

	@DslName("getByMapWithutTemplate")
	public Map getByMapWithutTemplate(Map test) {
		return test;
	}
	@DslName("getByDeepMap")
	public Map getByDeepMap(Map test) {
		return test;
	}
	@DslName("getByMapList")
	public List getByMapList(List test) {
		return test;
	}

}
