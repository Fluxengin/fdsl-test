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
        log.debug("UserInfoVariant:" + id);
        //ダミ－データ
        Map<String, Object> m = new HashMap<>();
        String userId;
        switch (id) {
            case "batch":
            case "publish":
                userId = id;
                break;
            default:
                userId = "uid12345";
                break;
        }
        m.put("ユーザーID", userId);
        m.put("パケット上限", Long.valueOf(5368709120l));
        return m;
    }
}
