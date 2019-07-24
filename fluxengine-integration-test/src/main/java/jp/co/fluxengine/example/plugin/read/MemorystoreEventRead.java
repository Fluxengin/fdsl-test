package jp.co.fluxengine.example.plugin.read;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Read;
import jp.co.fluxengine.stateengine.plugin.Fetch;

import java.util.List;
import java.util.Map;

@Read("memorystore/Memorystoreの内容取得#Memorystore取得Read")
public class MemorystoreEventRead implements Fetch {

    private List<Map<String, Object>> data = Lists.newArrayList();

    private int cursor = 0;

    @DslName("get")
    public void createList(String requestId) {
        Map<String, Object> element = Maps.newHashMap();
        element.put("requestid", requestId);
        data.add(element);
        cursor = 0;
    }

    @Override
    public Map<String, Object> fetch() {
        if (cursor < data.size()) {
            Map<String, Object> result = data.get(cursor);
            cursor++;
            return result;
        } else {
            return null;
        }
    }
}
