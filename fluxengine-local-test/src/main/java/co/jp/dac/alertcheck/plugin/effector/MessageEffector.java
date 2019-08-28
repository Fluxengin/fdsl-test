package co.jp.dac.alertcheck.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

/**
 * Project Name : AlertCheck
 * Creation Date : 2019/01/23
 * Author : zhangxin
 * Modified date : 2019/01/23
 * Modifier : zhangxin
 * Description: check result and store msgWW
 * 2019　D.A.Consortium All rights reserved.
 */
@Effector("effector/MessageEffector#msgEffector")
public class MessageEffector {

	private static final Logger log = LogManager.getLogger(MessageEffector.class);
	//広告案件名
	@DslName("adProject")
	private String adProject;
	//媒体名
    @DslName("media")
	private String media;
    //アカウントId
	@DslName("accountId")
	private long accountId;
	//キャンペーンId
    @DslName("campaignId")
    private long campaignId;
	//キャンペーンId
	@DslName("keywordId")
	private long keywordId;
	//広告グループId
	@DslName("adGroupId")
	private long adGroupId;
	//チェック日時(YYYYMMdd HH:SS)
	@DslName("checkedDateTime")
	private LocalDateTime checkedDateTime;
	//アラートメッセージ
	@DslName("alertMessage")
	private String alertMessage;
	//チェック項目ID
	@DslName("checkItemId")
	private Integer checkItemId;
	//広告案件SEQ
	@DslName("adProjectSeq")
	private long adProjectSeq;
	//カテゴリグループID
	@DslName("categoryGroupId")
	private long categoryGroupId;
	//カテゴリソート順
	@DslName("categorySort")
	private String categorySort;
	//カテゴリ階層番号
	@DslName("kategoryNo")
	private long kategoryNo;
	//広告ID
	@DslName("adId")
	private long adId;
	//時間閾値SEQ
	@DslName("timeThresholdId")
	private long timeThresholdId;
	//日閾値SEQ
	@DslName("dayThresholdId")
	private long dayThresholdId;
	//カテゴリKPI日閾値SEQ
	@DslName("dayKpiThresholdId")
	private long dayKpiThresholdId;

	@Post
	public void post() {

	}

}