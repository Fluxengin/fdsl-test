package jp.co.fluxengine.example.util;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.common.collect.Iterators;

import java.util.Arrays;

public class DatastoreCleaner {

    public static void main(String[] args) {
        String namespace = System.getenv("NAMESPACE");
        String[] kinds = {"DSL", "EFFECTOR", "ILLEGAL_EVENT", "PERSISTER"};

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        Key[] targetKeys = Arrays.stream(kinds)
                .map(kind -> Query.newKeyQueryBuilder().setNamespace(namespace).setKind(kind).build())
                .map(datastore::run)
                .flatMap(queryResult -> Arrays.stream(Iterators.toArray(queryResult, Key.class)))
                .toArray(Key[]::new);

        if (targetKeys.length > 0) {
            System.out.println("以下のキーを削除します");
            System.out.println(Arrays.toString(targetKeys));

            datastore.delete(targetKeys);
        } else {
            System.out.println("削除するキーはありませんでした");
        }
    }

}
