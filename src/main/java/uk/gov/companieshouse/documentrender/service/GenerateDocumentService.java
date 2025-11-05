package uk.gov.companieshouse.documentrender.service;

import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.logging.Logger;

@Service
public class GenerateDocumentService {

    private final Logger logger;

    public GenerateDocumentService(final Logger logger) {
        this.logger = logger;
    }

    public Optional<Void> render(final String html, final Map<String, String> data) {
        logger.trace("render(html=%s, data=%s) method called.".formatted(html, data));

        return Optional.empty();
    }

}
