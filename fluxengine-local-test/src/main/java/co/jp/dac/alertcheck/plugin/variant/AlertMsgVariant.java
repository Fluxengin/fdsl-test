package co.jp.dac.alertcheck.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;

@Variant("variant/AlertMsgVariant#alertMsg")
public class AlertMsgVariant {

    private static Log log = LogFactory.getLog(AlertMsgVariant.class);

    @DslName("get")
    public String get(BigDecimal checkNo) {
//        CommonSqlMapper common = SpringUtils.getBean(CommonSqlMapper.class);
//        String alertMsg = common.getNotifiyMsg(checkNo);
//        if(alertMsg != null && !"".equals(alertMsg.trim())){
//            return common.getNotifiyMsg(checkNo);
//        }
       // log.debug("checkNo:" + alertMsg + "alertMsg didn't define in mysql");
        return "";
    }
}
