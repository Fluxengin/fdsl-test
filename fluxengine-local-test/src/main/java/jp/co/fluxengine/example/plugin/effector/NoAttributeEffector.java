package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Effector("属性無し#通知")
public class NoAttributeEffector {

    private static final Logger log = LoggerFactory.getLogger(NoAttributeEffector.class);

    @Post
    public void post() {
        log.debug("属性無しのEffector NoAttributeEffectorが実行されました");
    }
}
