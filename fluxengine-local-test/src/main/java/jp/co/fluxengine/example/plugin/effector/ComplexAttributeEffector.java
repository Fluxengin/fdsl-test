package jp.co.fluxengine.example.plugin.effector;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Effector("複雑な属性#ff")
public class ComplexAttributeEffector {

  private static final Logger log = LogManager.getLogger(ComplexAttributeEffector.class);

  @DslName("a1")
  private Map<String, Object> a1;

  @DslName("a2")
  private Map<String, Object> a2;

  @DslName("a3")
  private List<String> a3;

  @DslName("a4")
  private List<Map<String, Object>> a4;

  @Post
  public void post() {
    log.info("a11 = " + a1.get("a11"));
    log.info("a12 = " + a1.get("a12"));
    log.info("a21 = " + a2.get("a21"));
    Map<String, String> a22 = (Map<String, String>) a2.get("a22");
    log.info("a221 = " + a22.get("a221"));
    log.info("a222 = " + a22.get("a222"));
    log.info("a3 = " + String.join(",", a3));
    String a4String = a4.stream().map(map -> "a41=" + map.get("a41") + ",a42=" + map.get("a42") + ",a43=" + map.get("a43")).collect(
        Collectors.joining(","));
    log.info("a4 = " + a4String);
  }
}
