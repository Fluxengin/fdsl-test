package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Variant("variant/ユーザー情報#List情報")
public class TestListInfoVariant {

	private static final Logger log = LogManager.getLogger(TestListInfoVariant.class);
	private int count=0;

	@DslName("get")
	public Map<String, Object> get(List<Map> test) {
		count++;
		log.debug("List情報 " + count +"回目 :"  + test);
		//ダミ－データ
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("ユーザーID", "uid12345");
		m.put("パケット上限", Long.valueOf(5368709120l));
		return m;
	}

}
