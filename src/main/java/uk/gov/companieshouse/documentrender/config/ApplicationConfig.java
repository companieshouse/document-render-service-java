package uk.gov.companieshouse.documentrender.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.companieshouse.environment.EnvironmentReader;
import uk.gov.companieshouse.environment.impl.EnvironmentReaderImpl;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Configuration
public class ApplicationConfig {

    public static final String NAMESPACE = "document-render-service-java";

    @Bean
    public Logger logger(@Value("${spring.application.name}") String applicationName) {
        return LoggerFactory.getLogger(applicationName);
    }

    @Bean
    EnvironmentReader environmentReader(Logger logger) {
        logger.trace("environmentReader() method called.");

        return new EnvironmentReaderImpl();
    }

    @Bean
    ObjectMapper objectMapper(Logger logger) {
        logger.trace("objectMapper() method called.");

        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

}
