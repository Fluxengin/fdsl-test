package jp.co.fluxengine.example.plugin.read;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Read;
import jp.co.fluxengine.stateengine.plugin.Fetch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Read("rule/日別データ検証#日別データ")
public class DailyDataReader implements Fetch {

	private static final Logger LOG = LogManager.getLogger(DailyDataReader.class);

	private List<Object> cursor;

	private int index = 0;

	private int length;

	@DslName("get")
	public void getList(String id) throws InterruptedException {
		LOG.debug("getList 開始");

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
		LOG.debug("getList 終了");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> fetch() {
		LOG.debug("fetch index = " + index);

		if (index >= length) {
			return null;
		}
		Map<String, Object> obj = (Map<String, Object>) cursor.get(index);
		index++;
		return obj;
	}

}
