package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Effector("ユーザー通知#メール送信")
public class MailNotificationEffector {

    private static final Logger log = LoggerFactory.getLogger(MailNotificationEffector.class);

    @DslName("ユーザーID")
    private String userId;

    @DslName("日時")
    private LocalDateTime now;

    @DslName("メッセージ")
    private String message;

    @Post
    public void post() {
        StringBuilder sb = new StringBuilder();
        sb.append("■■アラート:");
        sb.append(now);
        sb.append(" ");
        sb.append(message);
        log.debug(sb.toString());
    }
}