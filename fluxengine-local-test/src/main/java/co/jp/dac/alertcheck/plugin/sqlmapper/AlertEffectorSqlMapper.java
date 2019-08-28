package co.jp.dac.alertcheck.plugin.sqlmapper;

import java.util.Map;

/**
 * Project Name : AlertCheck
 * Creation Date : 2019/01/23
 * Author : zhangxin
 * Modified date : 2019/01/23
 * Modifier : zhangxin
 * Description: store alert msg
 * 2019　D.A.Consortium All rights reserved.
 */
public interface AlertEffectorSqlMapper {
    /*
     * store alert msg
     */
    public void addAlertMsg(Map msgMap);
}
