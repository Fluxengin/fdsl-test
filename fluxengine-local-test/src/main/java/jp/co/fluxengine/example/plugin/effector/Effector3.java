package jp.co.fluxengine.example.plugin.effector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;

@Effector("07_watchの引数間#f3")
public class Effector3 {

	private static final Logger log = LogManager.getLogger(Effector3.class);

	@DslName("c")
	private String c;
	
	@DslName("v")
	private double v;
	
	@Post
	public void post() {
		log.debug("Effector3 c = " + c + ", v = " + v);
	}
	
}
