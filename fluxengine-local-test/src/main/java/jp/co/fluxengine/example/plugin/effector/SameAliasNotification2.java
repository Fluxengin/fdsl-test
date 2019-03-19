package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Effector("違うeffectorが同じ別名#f2")
public class SameAliasNotification2 {

  private static final Logger log = LogManager.getLogger(SameAliasNotification2.class);

  @DslName("message")
  private String message;

  @Post
  public void post() {
    log.debug("メッセージ " + message);
  }
}
