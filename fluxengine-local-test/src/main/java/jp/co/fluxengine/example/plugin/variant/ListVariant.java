package jp.co.fluxengine.example.plugin.variant;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Variant;

import java.util.Arrays;
import java.util.List;

@Variant("listのマスタ参照#userlist_type_info")
public class ListVariant {

    @DslName("get")
    public List<String> get(String input) {
        return Arrays.asList(input + "0", input + "1", input + "2");
    }

}
