package jp.co.fluxengine.example.plugin.effector;

import java.util.concurrent.atomic.AtomicInteger;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Effector("エイリアスのインスタンス#ログ出力")
public class ReusableAliasEffector {

  private static final Logger log = LogManager.getLogger(ReusableAliasEffector.class);

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
