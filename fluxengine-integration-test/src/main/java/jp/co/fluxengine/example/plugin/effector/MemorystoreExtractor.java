package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.example.CloudSqlPool;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Effector("memorystore/Memorystoreの内容取得#Memorystore取得")
public class MemorystoreExtractor {

    private static final Logger log = LogManager.getLogger(MemorystoreExtractor.class);

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
                    keys.stream().distinct().map(keyString -> keyString.getBytes()).toArray(size -> new byte[size][]);

            if (targetKeys.length == 0) {
                log.info("No key exists");
                return;
            }

            List<byte[]> values = jedis.mget(targetKeys);

            for (int i = 0; i < targetKeys.length; i++) {
                String key = new String(targetKeys[i]);
                byte[] value = values.get(i);

                log.debug("insert key = {}", key);

                PreparedStatement insert = conn.prepareStatement("INSERT INTO `memorystore_contents` (`requestid`, `key`, `value`) VALUES (?, ?, ?)");

                insert.setString(1, requestId);
                insert.setString(2, key);
                insert.setBinaryStream(3, new ByteArrayInputStream(value));

                insert.execute();
            }
        } catch (SQLException e) {
            log.error("error in MemorystoreExtractor#post", e);
        }
    }

    private static JedisPool initConnectionPool() {
        Properties props = new Properties();

        try (InputStream in = MemorystoreExtractor.class.getResourceAsStream("/fluxengine.properties")) {
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
