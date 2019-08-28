package co.jp.dac.alertcheck.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Project Name : AlertCheck
 * Creation Date : 2019/01/26
 * Author : zhangxin
 * Modified date : 2019/01/28
 * Modifier : zhangxin
 * Description: get alert check account
 * 2019　D.A.Consortium All rights reserved.
 */
@Variant("variant/MonthBudgetVariant#monthBudget")
public class MonthBudgetVariant {

	private static final Logger log = LoggerFactory.getLogger(MonthBudgetVariant.class);

	@DslName("get")
	@SuppressWarnings("unchecked")
	public Double get(Map<String, Object> condition) {
		return 1.0;
	}
}
