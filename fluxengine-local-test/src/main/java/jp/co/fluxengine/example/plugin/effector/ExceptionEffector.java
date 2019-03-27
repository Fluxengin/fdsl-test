package jp.co.fluxengine.example.plugin.effector;

import java.io.IOException;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;

@Effector("例外の記録#f1")
public class ExceptionEffector {

  @DslName("code")
  private String code;

  @Post
  public void post() throws IOException {
    switch (code) {
      case "IO":
        throw new IOException();
      default:
        throw new NullPointerException();
    }
  }
}
