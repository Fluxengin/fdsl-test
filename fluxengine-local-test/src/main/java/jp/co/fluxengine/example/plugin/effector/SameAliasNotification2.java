package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Effector("違うeffectorが同じ別名#f2")
public class SameAliasNotification2 {

    private static final Logger log = LoggerFactory.getLogger(SameAliasNotification2.class);

    @DslName("message")
    private String message;

    @Post
    public void post() {
        log.debug("メッセージ " + message);
    }
}
