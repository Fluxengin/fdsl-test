package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

@Effector("エイリアスのインスタンス#ログ出力")
public class ReusableAliasEffector {

    private static final Logger log = LoggerFactory.getLogger(ReusableAliasEffector.class);

    @DslName("message")
    private String message;

    private AtomicInteger counter = new AtomicInteger(0);

    private static AtomicInteger staticCounter = new AtomicInteger(0);

    @Post
    public void post() {
        int currentInstance = counter.incrementAndGet();
        int currentClass = staticCounter.incrementAndGet();
        log.debug("メッセージ = " + message);
        log.debug("インスタンス" + currentInstance + "回目");
        log.debug("クラス" + currentClass + "回目");
    }

}
