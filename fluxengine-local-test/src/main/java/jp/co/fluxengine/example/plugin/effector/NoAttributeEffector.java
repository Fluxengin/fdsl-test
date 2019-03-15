package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Effector("属性無し#通知")
public class NoAttributeEffector {

  private static final Logger log = LogManager.getLogger(NoAttributeEffector.class);

  @Post
  public void post() {
    log.debug("属性無しのEffector NoAttributeEffectorが実行されました");
  }
}
