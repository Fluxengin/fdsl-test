package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Variant("キャッシュ#回数カウントキャッシュON,キャッシュ#回数カウントキャッシュOFF")
public class CountableVariant {

    private static final Map<String, AtomicInteger> count = new ConcurrentHashMap<>();

    @DslName("get")
    public String get(String key) {
        count.putIfAbsent(key, new AtomicInteger(0));
        int currentCount = count.get(key).incrementAndGet();
        return key + currentCount;
    }

}
