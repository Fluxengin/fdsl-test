package jp.co.fluxengine.example.plugin.variant;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.fluxengine.stateengine.annotation.Variant;

@Variant("メソッドにアノテーション忘れ#createDate")
public class PluginCreateDateVariant {

	public Date get() {
		try {
			return new SimpleDateFormat("yyyy/MM/dd").parse("2019/02/28");
		} catch (Exception e) {
			return null;
		}
	}

}
