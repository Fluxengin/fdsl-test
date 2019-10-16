package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.example.CloudSqlPool;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Effector("effector_check/エフェクタ動作確認#DB書き込み送信")
public class WriteToCloudSqlEffector1 {

    private static final Logger log = LoggerFactory.getLogger(WriteToCloudSqlEffector1.class);

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
        try (Connection conn = CloudSqlPool.getDataSource().getConnection();
             PreparedStatement insert = conn.prepareStatement(
                     "INSERT INTO integration_test_effector (userid, message, createTime) VALUES (?, ? ,? );"
             )
        ) {

            insert.setString(1, userId);
            insert.setString(2, message);
            insert.setTimestamp(3, Timestamp.valueOf(now));

            insert.execute();
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
