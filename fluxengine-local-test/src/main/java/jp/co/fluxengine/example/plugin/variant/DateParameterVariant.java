package jp.co.fluxengine.example.plugin.variant;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("日付型のパラメタ#日付")
public class DateParameterVariant {

  @DslName("get")
  public String get(LocalDate date) {
    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }
}
