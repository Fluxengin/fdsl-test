package jp.co.fluxengine.example.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.util.AnnotationUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;
import java.util.Properties;

public class EnabledIfPersisterDbIsCondition implements ExecutionCondition {

    private static final Logger LOG = LogManager.getLogger(EnabledIfPersisterDbIsCondition.class);

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Optional<AnnotatedElement> element = context.getElement();
        Optional<EnabledIfPersisterDbIs> enabledAnnotation = AnnotationUtils.findAnnotation(element, EnabledIfPersisterDbIs.class);

        return enabledAnnotation.map(EnabledIfPersisterDbIs::value).map(dbType -> {
            try (InputStream in = EnabledIfPersisterDbIsCondition.class.getResourceAsStream("/fluxengine.properties")) {
                Properties props = new Properties();
                props.load(in);

                return dbType.equals(props.getProperty("persister.db")) ?
                        ConditionEvaluationResult.enabled("@EnabledIfPersisterDbIs value matched persister.db [" + dbType + "]") :
                        ConditionEvaluationResult.disabled("@EnabledIfPersisterDbIs value [" + dbType + "] didn't match persister.db [" + props.getProperty("persister.db") + "]");
            } catch (IOException e) {
                LOG.error("error in evaluateExecutionCondition", e);
                return ConditionEvaluationResult.disabled("error: " + e.getMessage());
            }
        }).orElse(ConditionEvaluationResult.enabled("@EnabledIfPersisterDbIs is not present"));
    }

}
