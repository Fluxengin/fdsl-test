package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Variant("variant値の変更#プラグインへのパラメタ変更_キャッシュ無効,variant値の変更#プラグインへのパラメタ変更_キャッシュ有効")
public class DslReplacementVariant {

    private static final Logger log = LoggerFactory.getLogger(DslReplacementVariant.class);

    @DslName("get")
    public String get(String message) {
        log.debug("受け取ったメッセージ: {}", message);

        String result = message + " accepted";

        log.debug("返すメッセージ: {}", result);

        return result;
    }
}
