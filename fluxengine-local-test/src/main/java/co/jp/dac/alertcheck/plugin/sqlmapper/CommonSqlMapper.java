package co.jp.dac.alertcheck.plugin.sqlmapper;

import java.util.List;
import java.util.Map;

/**
 * Project Name : AlertCheck
 * Creation Date : 2019/01/25
 * Author : zhangxin
 * Modified date : 2019/01/25
 * Modifier : zhangxin
 * Description: alertCheck Common tools
 * 2019　D.A.Consortium All rights reserved.
 */
public interface CommonSqlMapper {
    /*
     * get the check flag
     */
    public String getAllCheckFlag(int checkNo);

    /*
     * get the media name
     */
    public String getMediaName(int checkNo);

    /*
     * get the gorin kind
     */
    public String getGorinKind(int checkNo);

    /*
     * get the gorin term
     */
    public List<Map<String, Object>> getTermJudgment(Map<String, Object> condition);

    /*
     * get the notice message
     */
    public String getNotifiyMsg(int checkNo);

    /*
     * get the target account
     */
    public Map<String, Object> getAccount(Map<String, Object> condition);

    /*
     * get the crew alertCheck object
     */
    public Map<String, Object> getCrewCheckDetail(Map<String, Object> condition);

    /*
     * get the crew alertCheck object
     */
    public Map<String, Object> getCrewCheckAdvertising(Map<String, Object> condition);

    /*
     * get the month budget
     */
    public Map<String, Object> getMonthBudget(Map<String, Object> condition);
}
