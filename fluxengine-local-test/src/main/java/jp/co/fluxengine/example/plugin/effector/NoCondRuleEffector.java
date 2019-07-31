package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Effector("条件なし#f1")
public class NoCondRuleEffector {

    private static final Logger log = LoggerFactory.getLogger(NoCondRuleEffector.class);

    @DslName("message")
    private String message;

    @Post
    public void post() {
        log.debug("メッセージ = " + message);
    }
}
