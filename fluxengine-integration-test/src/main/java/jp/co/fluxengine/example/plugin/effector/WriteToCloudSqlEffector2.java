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
import java.time.Instant;

@Effector("effector_check/エフェクタ動作確認#DB書き込み送信2")
public class WriteToCloudSqlEffector2 {

    private static final Logger log = LoggerFactory.getLogger(WriteToCloudSqlEffector2.class);

    @DslName("ユーザーID")
    private String userId;

    // DSL型に対応するJava型のスーパークラスやインタフェースでも受け取れるかのテスト
    // であるが、state-engineが対応しておらず(優先度: 低)、DSLの登録時にエラーとなるため、いったん同じ型にする
    // TODO https://trello.com/c/I8fodXZ9

    @DslName("数値")
//    private Number numberValue;
    private int numberValue;

    @DslName("メッセージ")
//    private CharSequence message;
    private String message;

    @Post
    public void post() {
        try (Connection conn = CloudSqlPool.getDataSource().getConnection();
             PreparedStatement insert = conn.prepareStatement(
                     "INSERT INTO integration_test_effector (userid, message, createTime) VALUES (?, ? ,? );"
             )
        ) {

            insert.setString(1, userId);
            String insertedMessage = new StringBuilder(message).append(" : ").append(numberValue).toString();
            insert.setString(2, insertedMessage);
            insert.setTimestamp(3, Timestamp.from(Instant.now()));

            insert.execute();

            log.debug("テーブル integration_test_effector に書き込みました ユーザID=[{}], メッセージ=[{}]", userId, insertedMessage);
        } catch (SQLException e) {
            log.error("post実行中にエラーが発生しました", e);
            throw new RuntimeException(e);
        }
    }
}
