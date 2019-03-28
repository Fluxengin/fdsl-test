package jp.co.fluxengine.example.plugin.variant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("日時型のパラメタ#日時")
public class DatetimeParameterVariant {

  @DslName("get")
  public String get(LocalDateTime date) {
    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH_mm_ss"));
  }

}
