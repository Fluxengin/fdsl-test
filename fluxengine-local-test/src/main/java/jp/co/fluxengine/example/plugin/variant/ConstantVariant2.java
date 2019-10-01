package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

import java.math.BigInteger;

@Variant("条件分岐#r")
public class ConstantVariant2 {

    @DslName("get")
    public int get() {
        return 10000;
    }
}
