package jp.co.fluxengine.example;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.DataflowScopes;
import com.google.api.services.dataflow.model.LaunchTemplateParameters;
import com.google.api.services.dataflow.model.LaunchTemplateResponse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jp.co.fluxengine.stateengine.model.datom.Event;
import jp.co.fluxengine.stateengine.util.JacksonUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "FluxengineIntegrationTestMemorystore", value = "/fluxengine-integration-test-memorystore")
public class MemorystoreServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(MemorystoreServlet.class.getName());

    protected Dataflow dataflowService;

    protected Properties jobProperties = new Properties();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestId = req.getParameter("requestid");
        String keys = req.getParameter("keys");

        log.info("requestid = " + requestId);

        LaunchTemplateParameters parameters = new LaunchTemplateParameters();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("eventJacksonStr", createEventString(requestId, keys));
        paramMap.put("streaming", "false");
        parameters.setParameters(paramMap);

        parameters.setJobName("integrationTestMemorystore-" + requestId);

        Dataflow.Projects.Locations.Templates.Launch request = dataflowService.projects().locations().templates().launch(
                jobProperties.getProperty("projectId"),
                jobProperties.getProperty("region"),
                parameters
        );
        request.setGcsPath(jobProperties.getProperty("templateLocation"));
        request.setValidateOnly(false);

        LaunchTemplateResponse response = request.execute();

        try (PrintWriter writer = resp.getWriter()) {
            writer.println("JobId=" + response.getJob().getId());
        }
    }

    protected String createEventString(String requestId, String keys) {
        return JacksonUtils.writeValueAsString(createEvent(requestId, keys));
    }

    protected List<Event> createEvent(String requestId, String keys) {
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

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            dataflowService = new Dataflow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    GoogleCredential.getApplicationDefault().createScoped(Arrays.asList(DataflowScopes.COMPUTE, DataflowScopes.CLOUD_PLATFORM, DataflowScopes.COMPUTE_READONLY, DataflowScopes.USERINFO_EMAIL))
            ).build();
        } catch (GeneralSecurityException e) {
            log.log(Level.SEVERE, "error in init", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.log(Level.SEVERE, "error in init", e);
            throw new UncheckedIOException(e);
        }

        try (InputStream in = new FileInputStream(config.getServletContext().getRealPath("/") + "/job.properties")) {
            jobProperties.load(in);
        } catch (IOException e) {
            log.log(Level.SEVERE, "error in init", e);
            throw new UncheckedIOException(e);
        }
    }
}
