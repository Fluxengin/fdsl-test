package jp.co.fluxengine.example.plugin.variant;

import java.util.Map;
import java.util.stream.Collectors;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("マップ型のパラメタ#stringified")
public class MapParameterVariant {

  @DslName("convert")
  public String convert(Map<String, Object> map, String keyValueDelimiter,
      String attributeDelimiter) {
    return map.entrySet().stream()
        .map(entry -> entry.getKey() + keyValueDelimiter + entry.getValue()).collect(
            Collectors.joining(attributeDelimiter));
  }
}
