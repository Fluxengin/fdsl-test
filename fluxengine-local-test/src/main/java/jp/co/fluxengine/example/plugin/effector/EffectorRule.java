package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Effector("複数のwatch#f")
public class EffectorRule {

    private static final Logger log = LoggerFactory.getLogger(EffectorRule.class);

    @DslName("v")
    private int v;

    @Post
    public void post() {
        log.debug("EffectorRule v = " + v);
    }
}
