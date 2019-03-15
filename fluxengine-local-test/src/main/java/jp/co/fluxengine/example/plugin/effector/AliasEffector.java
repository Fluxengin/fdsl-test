package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Effector("エイリアス#通知")
public class AliasEffector {

  private static final Logger log = LogManager.getLogger(AliasEffector.class);

  @DslName("message")
  private String message;

  @Post
  public void post() {
    log.debug(message);
  }
}
