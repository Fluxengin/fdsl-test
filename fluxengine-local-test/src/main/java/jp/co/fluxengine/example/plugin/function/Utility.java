package jp.co.fluxengine.example.plugin.function;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Function("ユーティリティ")
public class Utility {

  private static final Logger log = LogManager.getLogger(Utility.class);

  @DslName("weekday")
  public static String weekday() {
    return "201852";
  }

  //DSLから呼び出すにはstaticメソッドである必要がある
  @DslName("文字列連結")
  public static String strCat(String str1, String str2) {
    return "ユーザー定義【" + str1 + ":" + str2 + "】";
  }

  @DslName("文字列連結２")
  public static String strCat2(String str1, String str2) {
    return "ユーザー定義 文字列連結２【" + str1 + ":" + str2 + "】";
  }

  @DslName("フォーマット")
  public static String format(String format, String param) {
    return format.replace("{0}", param);
  }

  @DslName("フォーマット2")
  public static String format(String format, String param, String param2) {
    return format.replace("{0}", param).replace("{1}", param2);
  }

  @DslName("状態判定")
  public static String match(String state, String compare, String tStr, String fStr) {
    return state.equals(compare) ? tStr : fStr;
  }

  @DslName("日付変換")
  public static LocalDate getLocalDate(String date) {
    return LocalDate.parse(date);
  }

  @DslName("日時変換")
  public static LocalDateTime getLocalDateTime(String datetime) {
    return LocalDateTime.parse(datetime);
  }

  @DslName("各種引数確認")
  public static boolean get(String str, Integer num, String enumValue, boolean bl, LocalDate date,
      LocalDateTime datetime) {

    StringBuilder sb = new StringBuilder();
    sb.append("\n--------------------------各種引数確認関数\n");
    sb.append("str:" + str + "\n");
    sb.append("num:" + num + "\n");
    sb.append("enumValue:" + enumValue + "\n");
    sb.append("bl:" + bl + "\n");
    sb.append("date:" + date + "\n");
    sb.append("datetime:" + datetime + "\n");
    sb.append("--------------------------\n");
    log.debug(sb.toString());

    return true;
  }

  @DslName("配列引数確認")
  public static boolean getArray(List<String> strList, List<Integer> numList) {
    StringBuilder sb = new StringBuilder();
    sb.append("\n--------------------------配列引数確認関数\n");
    sb.append("strList\n");
    for (String s : strList) {
      sb.append("  " + s + "\n");

    }
    sb.append("numList\n");
    for (Integer i : numList) {
      sb.append("  " + i + "\n");
    }
    sb.append("--------------------------\n");
    log.debug(sb.toString());

    return false;
  }

  @DslName("customsum2")
  public static int customsum2(int a, int b) {
    return a + b;
  }

  @DslName("root")
  public static double root(double n) {
    return Math.sqrt(n);
  }

  @DslName("listToString")
  public static String listToString(List<Object> list) {
    return list.stream().map(elem -> elem instanceof LocalDate ? ((LocalDate) elem).format(
        DateTimeFormatter.ofPattern("yyyy/MM/dd")) : elem.toString())
        .collect(Collectors.joining(", "));
  }

  @DslName("createList")
  public static List<Map<String, Object>> createList(String keyPrefix) {
    return Arrays.asList(
        map3(
            "key", keyPrefix + "1",
            "name", "a",
            "value", 1
        ),
        map3(
            "key", keyPrefix + "2",
            "name", "aa",
            "value", 2
        ),
        map3(
            "key", keyPrefix + "3",
            "name", "ccc",
            "value", 300
        )
    );
  }

  private static Map<String, Object> map3(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
    Map<String, Object> result = Maps.newHashMap();
    result.put(key1, value1);
    result.put(key2, value2);
    result.put(key3, value3);
    return result;
  }
}
