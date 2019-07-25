package jp.co.fluxengine.example;

import org.apache.logging.log4j.core.util.IOUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "FluxengineIntegrationTestEvent", value = "/fluxengine-integration-test-event")
public class ArbitraryEventServlet extends AbstractServlet {

    private static final Logger log = Logger.getLogger(ArbitraryEventServlet.class.getName());

    @Override
    protected String createEventString(HttpServletRequest req, String mode) {
        if (mode.equalsIgnoreCase("GET")) {
            return req.getParameter("events");
        } else {
            try {
                return IOUtils.toString(req.getReader());
            } catch (IOException e) {
                log.log(Level.SEVERE, "error in createEventString", e);
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
