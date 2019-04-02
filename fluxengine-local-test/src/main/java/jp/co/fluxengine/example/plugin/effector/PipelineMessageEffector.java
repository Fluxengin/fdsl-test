package jp.co.fluxengine.example.plugin.effector;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Effector("パイプライン全体のテスト#f")
public class PipelineMessageEffector {

  private static final Logger log = LogManager.getLogger(PipelineMessageEffector.class);

  @DslName("msg")
  private String msg;

  @DslName("currentDatetime")
  private LocalDateTime currentDatetime;

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  @Post
  public void post() {
    log.info(currentDatetime.format(formatter) + " " + msg);
  }
}
