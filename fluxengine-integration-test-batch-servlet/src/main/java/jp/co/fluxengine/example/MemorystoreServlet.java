package jp.co.fluxengine.example;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jp.co.fluxengine.stateengine.model.datom.Event;
import jp.co.fluxengine.stateengine.util.JacksonUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet(name = "FluxengineIntegrationTestMemorystore", value = "/fluxengine-integration-test-memorystore")
public class MemorystoreServlet extends AbstractServlet {

    private static final Logger log = Logger.getLogger(MemorystoreServlet.class.getName());

    @Override
    protected String createEventString(HttpServletRequest req, String mode) {
        if (!mode.equalsIgnoreCase("GET")) {
            throw new UnsupportedOperationException("it supports GET only");
        }

        String requestId = req.getParameter("requestid");
        String keys = req.getParameter("keys");

        log.info("requestid = " + requestId + ", keys = " + keys);

        return JacksonUtils.writeValueAsString(createEvent(requestId, keys));
    }

    @Override
    protected String getJobName(HttpServletRequest req) {
        String requestId = req.getParameter("requestid");
        return "integrationTestMemorystore-" + requestId;
    }

    private List<Event> createEvent(String requestId, String keys) {
        Event event = new Event();
        event.setNamespace("memorystore/Memorystoreの内容取得");
        event.setEventName("Memorystore取得イベント");
        event.setCreateTime(LocalDateTime.now());

        Map<String, Object> propertyMap = Maps.newHashMap();
        propertyMap.put("requestid", requestId);
        propertyMap.put("keys", (keys == null || keys.isEmpty()) ?
                Lists.newArrayList() :
                Arrays.asList(keys.split(",")));
        event.setProperty(propertyMap);

        List<Event> eventList = Lists.newArrayList();
        eventList.add(event);

        return eventList;
    }

}
