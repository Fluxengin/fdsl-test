package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Effector("エフェクタの無効化#ef1,エフェクタの無効化2_1#ef2,エフェクタの無効化2_2#ef2")
public class LoggingEffector {

    private static final Logger log = LoggerFactory.getLogger(LoggingEffector.class);

    @DslName("message")
    private String message;

    @Post
    public void post() {
        log.debug("メッセージ = " + message);
    }
}
