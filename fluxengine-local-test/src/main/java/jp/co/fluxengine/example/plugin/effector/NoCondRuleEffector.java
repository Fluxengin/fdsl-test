package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Effector("条件なし#f1")
public class NoCondRuleEffector {

  private static final Logger log = LogManager.getLogger(NoCondRuleEffector.class);

  @DslName("message")
  private String message;

  @Post
  public void post() {
    log.debug("メッセージ = " + message);
  }
}
