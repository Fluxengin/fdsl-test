package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Effector("07_watchの引数間#f5")
public class Effector5 {

    private static final Logger log = LoggerFactory.getLogger(Effector5.class);

    @DslName("c")
    private String c;

    @DslName("v")
    private double v;

    @Post
    public void post() {
        log.debug("Effector5 c = " + c + ", v = " + v);
    }

}
