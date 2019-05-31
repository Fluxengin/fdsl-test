package jp.co.fluxengine.example.plugin.read;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Read;
import jp.co.fluxengine.stateengine.plugin.Fetch;

@Read("rule/日別データ検証#日別データ")
public class DailyDataReader implements Fetch {

	private List<Object> cursor;

	private int index = 0;

	private int length;

	@DslName("get")
	public void getList(String id) throws InterruptedException {
		cursor = Lists.newArrayList();
		HashMap<String, Object> map = Maps.newHashMap();
		map.put("端末ID", "C01");
		map.put("日時", LocalDateTime.now());
		map.put("使用量", 500);

		HashMap<String, Object> map1 = Maps.newHashMap();
		map1.put("端末ID", "C01");
		map1.put("日時", LocalDateTime.now().plusSeconds(1));
		map1.put("使用量", 600);
		cursor.add(map);
		cursor.add(map1);
		index = 0;
		length = cursor.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> fetch() {

		if (index >= length) {
			return null;
		}
		Map<String, Object> obj = (Map<String, Object>) cursor.get(index);
		index++;
		return obj;
	}

}
