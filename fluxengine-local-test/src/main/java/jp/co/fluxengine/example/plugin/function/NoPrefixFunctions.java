package jp.co.fluxengine.example.plugin.function;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Function;

@Function()
public class NoPrefixFunctions {

  @DslName("twice")
  public static double twice(double i1) {
    return i1 * i1;
  }

}
