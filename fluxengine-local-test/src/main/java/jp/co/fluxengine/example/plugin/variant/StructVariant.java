package jp.co.fluxengine.example.plugin.variant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("複雑な構造#HogeStrc")
public class StructVariant {

  @DslName("get")
  public Map<String, Object> get() {
    Map<String, Object> result = new HashMap<>();
    result.put("name", "hoge");
    result.put("kind", "variant");

    List<String> records = new ArrayList<>();
    records.add("record1");
    records.add("record2");

    List<Map<String, Object>> keyValues = new ArrayList<>();
    keyValues.add(mapOf("key", "key1", "value", 10.01));
    keyValues.add(mapOf("key", "key2", "value", -100));

    Map<String, Object> optionalInfo = mapOf("records", records, "keyValues", keyValues);

    result.put("optionalInfo", optionalInfo);

    return result;
  }

  private static Map<String, Object> mapOf(String key1, Object value1, String key2, Object value2) {
    Map<String, Object> result = new HashMap<>();
    result.put(key1, value1);
    result.put(key2, value2);
    return result;
  }
}
