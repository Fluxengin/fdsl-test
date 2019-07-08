package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.Map;

@Effector("effector/ユーザー通知#メール送信")
public class MailNotificationEffector103 {

	private static final Logger log = LogManager.getLogger(MailNotificationEffector.class);

	@DslName("値")
	private double value;

	@DslName("ユーザーID")
	private String userId;

	@DslName("日時")
	private LocalDateTime now;

	@DslName("メッセージ")
	private String message;

	@DslName("m1")
	private Map m1;

	@DslName("BigInteger")
	private Long big_integer;

	@Post
	public void post() {
		System.out.println("big_integer        " + big_integer);
		System.out.println("値        " + value);
		System.out.println("ユーザーID        " + userId);

		if(m1 != null) {
			System.out.println(m1);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("■■アラート:");
		sb.append(now);
		sb.append(" ");
		sb.append(message);
		log.debug(sb.toString());
	}
}