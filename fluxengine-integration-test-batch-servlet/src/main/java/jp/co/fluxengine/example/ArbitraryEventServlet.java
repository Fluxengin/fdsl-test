package jp.co.fluxengine.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@WebServlet(name = "FluxengineIntegrationTestEvent", value = "/fluxengine-integration-test-event")
public class ArbitraryEventServlet extends AbstractServlet {

    private static final Logger log = LoggerFactory.getLogger(ArbitraryEventServlet.class);

    @Override
    protected String createEventString(HttpServletRequest req, String mode) {
        if (mode.equalsIgnoreCase("GET")) {
            return req.getParameter("events");
        } else {
            try {
                return req.getReader().lines().collect(Collectors.joining("\n"));
            } catch (IOException e) {
                log.error("error in createEventString", e);
                throw new UncheckedIOException(e);
            }
        }
    }

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    @Override
    protected String getJobName(HttpServletRequest req) {
        return "integrationTestEvent-" + LocalDateTime.now().format(formatter);
    }

}
