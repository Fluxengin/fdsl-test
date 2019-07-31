package jp.co.fluxengine.example.plugin.effector;

import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Effector("パイプライン全体のテスト#f")
public class PipelineMessageEffector {

    private static final Logger log = LoggerFactory.getLogger(PipelineMessageEffector.class);

    @DslName("msg")
    private String msg;

    @DslName("currentDatetime")
    private LocalDateTime currentDatetime;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Post
    public void post() {
        log.info(currentDatetime.format(formatter) + " " + msg);
    }
}
