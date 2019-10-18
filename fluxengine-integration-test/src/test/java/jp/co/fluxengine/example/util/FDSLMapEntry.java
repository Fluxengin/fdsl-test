package jp.co.fluxengine.example.util;

import java.util.Map;

public class FDSLMapEntry implements Map.Entry<String, Object> {
    private final String key;
    private final Object value;

    public FDSLMapEntry(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Object setValue(Object value) {
        throw new UnsupportedOperationException("値の変更はできません");
    }

    public static FDSLMapEntry of(String key, Object value) {
        return new FDSLMapEntry(key, value);
    }
}
