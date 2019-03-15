package jp.co.fluxengine.example.plugin.read;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Read;
import jp.co.fluxengine.stateengine.plugin.Fetch;

@Read("様々な型#全部の型")
public class AllTypesRead implements Fetch {

  @DslName("get")
  public void get() {
  }

  @Override
  public Map<String, Object> fetch() {
    Map<String, Object> result = Maps.newHashMap();
    result.put("真偽", true);
    result.put("数値", 123.45);
    result.put("文字列", "a string");
    result.put("日付", LocalDate.of(2019, 3, 13));
    result.put("日時", LocalDateTime.of(2019, 3, 13, 11, 22, 33));
    result.put("リスト", Lists.newArrayList(1, 2, 3));

    Map<String, String> map = Maps.newHashMap();
    map.put("キー1", "値1");

    result.put("マップ", map);

    return result;
  }
}
