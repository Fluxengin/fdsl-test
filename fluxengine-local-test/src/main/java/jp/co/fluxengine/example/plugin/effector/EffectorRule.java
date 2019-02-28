package jp.co.fluxengine.example.plugin.effector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;

@Effector("複数のwatch#f")
public class EffectorRule {

	private static final Logger log = LogManager.getLogger(EffectorRule.class);
	
	@DslName("v")
	private int v;
	
	@Post
	public void post() {
		log.debug("EffectorRule v = " + v);
	}
}
