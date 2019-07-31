package jp.co.fluxengine.example.plugin.effector;

import com.google.api.client.util.Lists;
import javafx.util.Pair;
import jp.co.fluxengine.example.CloudSqlPool;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Effector("memorystore/Memorystoreの内容取得#Memorystore取得")
public class MemorystoreExtractEffector {

    private static final Logger log = LoggerFactory.getLogger(MemorystoreExtractEffector.class);

    private static final JedisPool pool = initConnectionPool();

    @DslName("requestid")
    private String requestId;

    @DslName("keys")
    private List<String> keys;

    @Post
    public void post() {
        log.info("requestid = {}", requestId);

        // Redis to MySQL
        try (Jedis jedis = pool.getResource();
             Connection conn = CloudSqlPool.getDataSource().getConnection()) {
            byte[][] targetKeys = keys.size() == 0 ?
                    jedis.keys("*".getBytes()).toArray(new byte[0][]) :
                    keys.stream().distinct().map(String::getBytes).toArray(byte[][]::new);

            if (targetKeys.length == 0) {
                log.info("No key exists");
                insertNoDataRecord(conn, requestId);
                return;
            }

            List<byte[]> values = jedis.mget(targetKeys);

            List<Pair<String, byte[]>> data = Lists.newArrayList();

            for (int i = 0; i < targetKeys.length; i++) {
                byte[] value = values.get(i);

                if (value != null) {
                    String key = new String(targetKeys[i]);
                    data.add(new Pair<>(key, value));
                }
            }

            if (data.isEmpty()) {
                log.info("No data for keys: " + keys);
                insertNoDataRecord(conn, requestId);
                return;
            }

            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO `memorystore_contents` (`requestid`, `key`, `value`) VALUES " + StringUtils.repeat("(?,?,?)", ",", data.size()));

            for (int i = 0; i < data.size(); i++) {
                Pair<String, byte[]> keyValue = data.get(i);
                String key = keyValue.getKey();
                byte[] value = keyValue.getValue();

                insertStmt.setString(i * 3 + 1, requestId);
                insertStmt.setString(i * 3 + 2, key);
                insertStmt.setBinaryStream(i * 3 + 3, new ByteArrayInputStream(value));
            }

            insertStmt.execute();
        } catch (SQLException e) {
            log.error("error in MemorystoreExtractEffector#post", e);
        }
    }

    // データがないときは、未取得の時と区別するために、データがないことを示すレコード(keyが空文字列)を1件追加する
    private static void insertNoDataRecord(Connection conn, String requestId) throws SQLException {
        PreparedStatement insertNoEntries = conn.prepareStatement("INSERT INTO `memorystore_contents` (`requestid`, `key`, `value`) VALUES (?,?,?)");

        insertNoEntries.setString(1, requestId);
        insertNoEntries.setString(2, "");
        insertNoEntries.setBinaryStream(3, new ByteArrayInputStream("dummy".getBytes(StandardCharsets.UTF_8)));

        insertNoEntries.execute();
    }

    private static JedisPool initConnectionPool() {
        Properties props = new Properties();

        try (InputStream in = MemorystoreExtractEffector.class.getResourceAsStream("/fluxengine.properties")) {
            props.load(in);
        } catch (IOException e) {
            log.error("error in initConnectionPool", e);
            throw new UncheckedIOException(e);
        }

        String host = props.getProperty("persister.memorystore.host");
        int port = Integer.parseInt(props.getProperty("persister.memorystore.port"));

        log.debug("Memorystore host = {}, port = {}", host, port);

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxWaitMillis(10000);

        return new JedisPool(poolConfig, host, port, 10000);
    }
}
