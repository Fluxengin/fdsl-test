package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.example.CloudSqlPool;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Effector("effector_check/エフェクタ動作確認#DB書き込み送信")
public class MailNotificationCloudSqlEffector {

    private static final Logger log = LogManager.getLogger(MailNotificationCloudSqlEffector.class);

    @DslName("ユーザーID")
    private String userId;

    @DslName("日時")
    private LocalDateTime now;

    @DslName("メッセージ")
    private String message;

    @Post
    public void post() {

        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("■■アラート:");
            sb.append(now);
            sb.append(" ");
            sb.append(message);
            log.info(sb.toString());
        }
        try (Connection conn = CloudSqlPool.getDataSource().getConnection()) {

            // PreparedStatements can be more efficient and project against injections.
            PreparedStatement voteStmt = conn.prepareStatement(
                    "INSERT INTO integration_test_effector (userid, message, createTime) VALUES (?, ? ,? );");

            voteStmt.setString(1, userId);
            voteStmt.setString(2, message);
            voteStmt.setTimestamp(3, Timestamp.valueOf(now));


            // Finally, execute the statement. If it fails, an error will be thrown.
            voteStmt.execute();

        } catch (SQLException ex) {
            log.error("post実行中にエラーが発生しました", ex);
            throw new RuntimeException(ex);
        }
    }

}
/**
 * CREATE TABLE effector (userid VARCHAR(255), message VARCHAR(255),createTime timestamp ,
 * messageId INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(messageId));
 */
