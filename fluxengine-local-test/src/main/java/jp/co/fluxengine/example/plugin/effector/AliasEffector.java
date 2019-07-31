package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Effector("エイリアス#通知")
public class AliasEffector {

    private static final Logger log = LoggerFactory.getLogger(AliasEffector.class);

    @DslName("message")
    private String message;

    @Post
    public void post() {
        log.debug("メッセージ " + message);
    }
}
