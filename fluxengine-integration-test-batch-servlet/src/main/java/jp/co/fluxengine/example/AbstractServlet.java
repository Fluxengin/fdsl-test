package jp.co.fluxengine.example;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dataflow.Dataflow;
import com.google.api.services.dataflow.DataflowScopes;
import com.google.api.services.dataflow.model.LaunchTemplateParameters;
import com.google.api.services.dataflow.model.LaunchTemplateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class AbstractServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AbstractServlet.class);

    protected Dataflow dataflowService;

    protected Properties jobProperties = new Properties();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp, "GET");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp, "POST");
    }

    protected void process(HttpServletRequest req, HttpServletResponse resp, String mode) throws IOException {
        LaunchTemplateParameters parameters = new LaunchTemplateParameters();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("eventJacksonStr", createEventString(req, mode));
        paramMap.put("streaming", "false");
        parameters.setParameters(paramMap);

        parameters.setJobName(getJobName(req));

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

    protected abstract String createEventString(HttpServletRequest req, String mode);

    protected abstract String getJobName(HttpServletRequest req);

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
            log.error("error in init", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("error in init", e);
            throw new UncheckedIOException(e);
        }

        File jobPropFile = findFile(new File(config.getServletContext().getRealPath("/")), "job.properties");
        try (InputStream in = new FileInputStream(jobPropFile)) {
            jobProperties.load(in);
        } catch (IOException e) {
            log.error("error in init", e);
            throw new UncheckedIOException(e);
        }
    }

    private File findFile(File file, String targetFileName) {
        if (file.getName().equals(targetFileName)) {
            return file;
        } else if (file.isDirectory()) {
            for (File sub : file.listFiles()) {
                File subResult = findFile(sub, targetFileName);
                if (subResult != null) {
                    return subResult;
                }
            }
            return null;
        } else {
            return null;
        }
    }
}
