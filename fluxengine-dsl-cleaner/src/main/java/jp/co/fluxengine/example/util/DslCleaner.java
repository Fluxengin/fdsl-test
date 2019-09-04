package jp.co.fluxengine.example.util;

import com.google.cloud.datastore.*;
import com.google.common.collect.Iterators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class DslCleaner {
    private static final Logger LOG = LoggerFactory.getLogger(DslCleaner.class);

    private Datastore datastore;
    private String namespace;
    private String kind;

    public DslCleaner() throws IOException {
        datastore = DatastoreOptions.getDefaultInstance().getService();
        Properties dslProperties = loadProperties();
        namespace = dslProperties.getProperty("namespace");
        kind = dslProperties.getProperty("kind");
    }

    private Properties loadProperties() throws IOException {
        try (InputStream in = DslCleaner.class.getResourceAsStream("/dsl.properties")) {
            Properties props = new Properties();
            props.load(in);
            return props;
        }
    }

    public void cleanDsl() {
        KeyQuery query = Query.newKeyQueryBuilder()
                .setNamespace(namespace)
                .setKind(kind)
                .build();

        QueryResults<Key> resultsBefore = datastore.run(query);
        Key[] keysBefore = Iterators.toArray(resultsBefore, Key.class);

        if (keysBefore.length > 0) {
            LOG.info("次のDSLを消去します: {}", Arrays.toString(keysBefore));
            datastore.delete(keysBefore);
        } else {
            LOG.info("DSLはありませんでした");
        }
    }

    public static void main(String[] args) throws IOException {
        new DslCleaner().cleanDsl();
    }
}
