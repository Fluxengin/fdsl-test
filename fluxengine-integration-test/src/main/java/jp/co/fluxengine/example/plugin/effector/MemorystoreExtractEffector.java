package jp.co.fluxengine.example.plugin.effector;

import com.google.api.client.util.Lists;
import jp.co.fluxengine.example.CloudSqlPool;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import jp.co.fluxengine.stateengine.util.Serializer.KryoUtils;
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
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Effector("memorystore/Memorystoreの内容取得#Memorystore取得")
public class MemorystoreExtractEffector {

    private static final Logger log = LoggerFactory.getLogger(MemorystoreExtractEffector.class);

    private static final JedisPool pool = initConnectionPool();

    @DslName("requestid")
    private String requestId;

    @DslName("keys")
    private List<String> keys;

    private static class Pair<A, B> {
        public final A _1;
        public final B _2;

        public Pair(A _1, B _2) {
            this._1 = _1;
            this._2 = _2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(_1, pair._1) &&
                    Objects.equals(_2, pair._2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(_1, _2);
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "_1=" + _1 +
                    ", _2=" + _2 +
                    '}';
        }
    }

    @Post
    public void post() {
        log.info("requestid = {}, keys = {}", requestId, keys.toString());

        // Redis to MySQL
        try (Jedis jedis = pool.getResource();
             Connection conn = CloudSqlPool.getDataSource().getConnection()) {
            byte[][] targetKeys = keys.size() == 0 ?
                    jedis.keys("*".getBytes(StandardCharsets.UTF_8)).toArray(new byte[0][]) :
                    keys.stream().distinct().map(key -> key.getBytes(StandardCharsets.UTF_8)).toArray(byte[][]::new);

            if (targetKeys.length == 0) {
                log.info("No key exists");
                insertNoDataRecord(conn, requestId);
                return;
            }

            List<byte[]> values = jedis.mget(targetKeys);

            log.debug("{} keys and {} values obtained", targetKeys.length, values.size());

            List<Pair<String, byte[]>> data = Lists.newArrayList();

            for (int i = 0; i < targetKeys.length; i++) {
                byte[] value = values.get(i);

                String key = new String(targetKeys[i], StandardCharsets.UTF_8);
                log.debug("got key = {}, value = {}", key, value);
                if (value != null) {
                    data.add(new Pair<>(key, value));
                }
            }

            if (data.isEmpty()) {
                log.info("No data for keys: " + keys);
                insertNoDataRecord(conn, requestId);
                return;
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO `memorystore_contents` (`requestid`, `key`, `value`) VALUES " + StringUtils.repeat("(?,?,?)", ",", data.size()))
            ) {
                for (int i = 0; i < data.size(); i++) {
                    Pair<String, byte[]> keyValue = data.get(i);
                    String key = keyValue._1;
                    byte[] value = keyValue._2;

                    if (log.isDebugEnabled()) {
                        Map<String, Object> valueMap = KryoUtils.deserialize(value);
                        log.debug("inserting requestid = {}, key = {}, value = {}", requestId, key, valueMap == null ? "null" : valueMap.toString());
                    }

                    insertStmt.setString(i * 3 + 1, requestId);
                    insertStmt.setString(i * 3 + 2, key);
                    insertStmt.setBinaryStream(i * 3 + 3, new ByteArrayInputStream(value));
                }

                insertStmt.execute();
            }
        } catch (SQLException e) {
            log.error("error in MemorystoreExtractEffector#post", e);
        }
    }

    // データがないときは、未取得の時と区別するために、データがないことを示すレコード(keyが空文字列)を1件追加する
    private static void insertNoDataRecord(Connection conn, String requestId) throws SQLException {
        try (PreparedStatement insertNoEntries = conn.prepareStatement(
                "INSERT INTO `memorystore_contents` (`requestid`, `key`, `value`) VALUES (?,?,?)"
        )) {
            insertNoEntries.setString(1, requestId);
            insertNoEntries.setString(2, "");
            insertNoEntries.setBinaryStream(3, new ByteArrayInputStream("dummy".getBytes(StandardCharsets.UTF_8)));

            insertNoEntries.execute();
        }
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
