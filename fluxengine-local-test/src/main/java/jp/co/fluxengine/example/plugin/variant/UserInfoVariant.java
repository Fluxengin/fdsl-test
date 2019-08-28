package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Variant("variant/ユーザー情報#ユーザー情報")
public class UserInfoVariant {

    private static final Logger log = LoggerFactory.getLogger(UserInfoVariant.class);

    @DslName("get")
    public Map<String, Object> get(String id) {
        //TODO 永続化しているデータを取得したり、加工処理を記載
        log.debug("UserInfoVariant:" + id);
        //ダミ－データ
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("ユーザーID", "uid12345");
        m.put("パケット上限", Long.valueOf(5368709120l));
        return m;
    }

    // For Testing ver1.0.3 Map
    @DslName("getbyParameters")
    public Map<String, Object> get(Map test) {

        log.debug("UserInfoVariant:" + test.get("a1"));
        log.debug("UserInfoVariant:" + test.get("a2"));
        log.debug("UserInfoVariant:" + test.get("a3"));

        Map<String, Object> m = new HashMap<String, Object>();
        m.put("ユーザーID", "uid12345");
        m.put("パケット上限", Long.valueOf(5368709120l));
        return m;
    }
}
