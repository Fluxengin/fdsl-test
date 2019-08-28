package co.jp.dac.alertcheck.plugin.variant;

import co.jp.dac.alertcheck.plugin.common.IConstants;
import co.jp.dac.alertcheck.plugin.sqlmapper.CommonSqlMapper;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
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
@Variant("variant/AccountVariant#account")
public class AccountVariant {

	private static final Logger log = LoggerFactory.getLogger(AccountVariant.class);

	@DslName("get")
	public Map<String, Object> get(Map<String, Object> condition) {
		return getAccountResult(condition);
	}

	public Map<String, Object> getAccountResult(Map<String, Object> condition) {
		int checkNo = Integer.parseInt(condition.get("checkNo").toString());
		String execDay = condition.get("execDay").toString();
		int year = Integer.parseInt(execDay.substring(0, 4));
		return null;
//		CommonSqlMapper common = SpringUtils.getBean(CommonSqlMapper.class);
//		//配信媒体マスタから媒体名を取得
//		String mediaName = common.getMediaName(checkNo);
//		log.debug("配信媒体 媒体名:======>" + mediaName);
//		condition.put("mediaName", mediaName);
//		//チェック項目マスタから全体チェックフラグを取得
//		String checkFlag = common.getAllCheckFlag(checkNo);
//		log.debug("全体チェックフラグ:======>" + checkFlag);
//		// 0: 個別  1: 全体
//		if (checkFlag != null && "0".equals(checkFlag)) {
//			//個別のアラートチェック 処理
//			return getCheckAccount(condition, IConstants.ACCOUNT_RESPECTIVE ,common);
//		} else {
//			//チェック項目マスタからGorin種類を取得
//			String gorinKind = common.getGorinKind(checkNo);
//			log.debug("Gorin種類:======>" + gorinKind);
//			Map<String, Object> gorinCondition = new HashMap<>();
//			gorinCondition.put("year", year);
//			gorinCondition.put(IConstants.GORIN_KIND, gorinKind);
//			if (StringUtils.isBlank(gorinKind)) {
//				//(Gorinでもない Yahoo　Coreでもない場合) 全体のアラートチェック 処理
//				return getCheckAccount(condition, IConstants.ACCOUNT_CREW ,common);
//			} else {
//				//1.Gorinの場合
//				if (IConstants.GROIN_KIND_1.equals(gorinKind)) {
//					boolean inGorinTerm = isExistTerm(gorinCondition, common);
//					//実行日IN Groin期間
//					if (inGorinTerm) {
//						if (IConstants.MEDIA_NAME_GDN.equals(mediaName) || IConstants.MEDIA_NAME_GSEARCH.equals(mediaName)) {
//							//GDN or Gサーチの場合  全体のアラートチェック 処理
//							return getCheckAccount(condition, IConstants.ACCOUNT_CREW ,common);
//						} else {
//							//YSS or YDNの場合 個別のアラートチェック 処理
//							return getCheckAccount(condition, IConstants.ACCOUNT_RESPECTIVE ,common);
//						}
//					} else {
//						// 実行日NOT IN　Groin期間  個別のアラートチェック 処理
//						return getCheckAccount(condition, IConstants.ACCOUNT_RESPECTIVE ,common);
//					}
//				} else if (IConstants.GROIN_KIND_2.equals(gorinKind)) {
//					boolean inYahooCoreTerm = isExistTerm(gorinCondition,common);
//					//実行日IN Yahoo Core期間
//					if (inYahooCoreTerm) {
//						if (IConstants.MEDIA_NAME_YSS.equals(mediaName) || IConstants.MEDIA_NAME_YDN.equals(mediaName)) {
//							//YSS or YDNの場合 全体のアラートチェック 処理
//							return getCheckAccount(condition, IConstants.ACCOUNT_CREW ,common);
//						} else {
//							//GDN or Gサーチの場合 個別のアラートチェック 処理
//							return getCheckAccount(condition, IConstants.ACCOUNT_RESPECTIVE ,common);
//						}
//					} else {
//						// 実行日NOT IN　Yahoo Core期間 個別のアラートチェック 処理
//						return getCheckAccount(condition, IConstants.ACCOUNT_RESPECTIVE ,common);
//					}
//				} else {
//					//GROIN_KIND_2=3
//					//Gorin サーチの場合
//					Map<String, Object> gorinCondition1 = new HashMap<>();
//					gorinCondition1.put("year", execDay);
//					gorinCondition1.put("gorinCoreKind", 1);
//					boolean inGorinTerm = isExistTerm(gorinCondition1, common);
//					//Yahoo サーチの場合
//					Map<String, Object> gorinCondition2 = new HashMap<>();
//					gorinCondition2.put("year", execDay);
//					gorinCondition2.put("gorinCoreKind", 2);
//					boolean inYahooCoreTerm = isExistTerm(gorinCondition2, common);
//					if (inGorinTerm && inYahooCoreTerm) {
//						// 実行日IN Gorin and 実行日IN Yahoo Core
//						//全体のアラートチェック 処理
//						return getCheckAccount(condition, IConstants.ACCOUNT_CREW ,common);
//					} else if (inGorinTerm) {
//						// 実行日IN Gorin and 実行日NOT IN Yahoo Core
//						if (IConstants.MEDIA_NAME_GDN.equals(mediaName) || IConstants.MEDIA_NAME_GSEARCH.equals(mediaName)) {
//							//GDN or Gサーチの場合  全体のアラートチェック 処理
//							return getCheckAccount(condition, IConstants.ACCOUNT_CREW ,common);
//						} else {
//							//YSS or YDNの場合 個別のアラートチェック 処理
//							return getCheckAccount(condition, IConstants.ACCOUNT_RESPECTIVE ,common);
//						}
//					} else if (inYahooCoreTerm) {
//						// 実行日NOT IN Gorin and 実行日IN Yahoo Core
//						if (IConstants.MEDIA_NAME_YSS.equals(mediaName) || IConstants.MEDIA_NAME_YDN.equals(mediaName)) {
//							//YSS or YDNの場合 全体のアラートチェック 処理
//							return getCheckAccount(condition, IConstants.ACCOUNT_CREW ,common);
//						} else {
//							//GDN or Gサーチの場合 個別のアラートチェック 処理
//							return getCheckAccount(condition, IConstants.ACCOUNT_RESPECTIVE ,common);
//						}
//					} else {
//						// 実行日NOT IN Gorin and 実行日NOT IN Yahoo Core
//						// 個別のアラートチェック 処理
//						return getCheckAccount(condition, IConstants.ACCOUNT_RESPECTIVE ,common);
//					}
//				}
//			}
//		}
	}

	/**
	 * judge checkDate is exist in biz term
	 * @param gorinCondition gorinCondition
	 * @param common sql mapper
	 * @return true/false
	 */
	public boolean isExistTerm(Map<String, Object> gorinCondition, CommonSqlMapper common) {
		List list = common.getTermJudgment(gorinCondition);
		if (list.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * get check account
	 * @param condition query condition
	 * @param common common sql mapper
	 * @return account data map
	 * @param flag  Respective/Crew
	 * @return account data map
	 */
	public Map<String, Object> getCheckAccount(
			Map<String, Object> condition, String flag, CommonSqlMapper common) {
		if (IConstants.ACCOUNT_RESPECTIVE.equals(flag)) {
			return getRespectiveAccount(condition, common);
		} else {
			return getCrewAccount(condition, common);
		}
	}

	/**
	 * getRespectiveAccount
	 * @param condition query condition
	 * @param common common sql mapper
	 * @return account data map
	 */
	public Map<String, Object> getRespectiveAccount(Map<String, Object> condition, CommonSqlMapper common){
		Map<String, Object> data = new HashMap<>();
		Map<String, Object> result = common.getAccount(condition);
		if(result == null){
			log.error("accountId:" + condition.get("accountId") + " hasn't threshold data");
			return null;
		}
		data.put("accountSeq", result.get("account_seq"));
		data.put("adComponentSeq", result.get("adComponent_seq"));
		data.put(IConstants.COMPONENT_NAME, result.get("adComponentName"));
		data.put("adProjectSeq", Long.parseLong(result.get("adProject_seq").toString()));
		data.put("upperLimitThreshold", result.get("upper_limit_threshold"));
		data.put("lowerLimitThreshold", result.get("lower_limit_threshold"));
		data.put("upperLimitThresholdRate", result.get("upper_limit_threshold_rate"));
		data.put("lowerLimitThresholdRate", result.get("lower_limit_threshold_rate"));
		data.put("dataReferencePeriod", result.get("data_reference_period"));
		return data;
	}

	/**
	 * getCrewAccount
	 * @param condition query condition
	 * @param common common sql mapper
	 * @return account data map
	 */
	public Map<String, Object> getCrewAccount(Map<String, Object> condition, CommonSqlMapper common){
		Map<String, Object> data = new HashMap<>();
		condition.put("componentTypeId", 2);
		condition.put("componentId", condition.get("accountId"));
		Map<String, Object> result1 = common.getCrewCheckDetail(condition);
		if(result1 == null){
			log.error("accountId:" + condition.get("accountId") + " hasn't threshold data");
			return null;
		}
		Map<String, Object> result2 = common.getCrewCheckAdvertising(condition);
		data.put("accountSeq", result2.get("account_seq"));
		data.put("adComponentSeq", result2.get("adComponent_seq"));
		data.put("adComponentName", result2.get("adComponentName"));
		data.put("adProjectSeq", Long.parseLong(result2.get("adProject_seq").toString()));
		data.put("upperLimitThreshold", result1.get("upper_limit_threshold"));
		data.put("lowerLimitThreshold", result1.get("lower_limit_threshold"));
		data.put("upperLimitThresholdRate", result1.get("upper_limit_threshold_rate"));
		data.put("lowerLimitThresholdRate", result1.get("lower_limit_threshold_rate"));
		data.put("dataReferencePeriod", result1.get("data_reference_period"));
		return data;
	}
}
