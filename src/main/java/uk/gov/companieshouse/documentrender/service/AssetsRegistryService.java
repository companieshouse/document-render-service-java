package uk.gov.companieshouse.documentrender.service;

import java.util.Optional;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.companieshouse.logging.Logger;

@Service
public class AssetsRegistryService {

    private final RestTemplate template;
    private final Logger logger;

    public AssetsRegistryService(final RestTemplate template, final Logger logger) {
        this.template = template;
        this.logger = logger;
    }

    public Optional<String> load(final String templateName, final String assetID) {
        logger.trace("load(templateName=%s, assetId=%s) method called.".formatted(templateName, assetID));
        try {
            String targetUri = "/assets/%s/templates/%s".formatted(assetID, templateName);

            ResponseEntity<String> response = template.exchange(targetUri, HttpMethod.GET, null, String.class);

            if (!response.getStatusCode().is2xxSuccessful() || !response.hasBody()) {
                logger.debug("Failed to load template from Assets Registry. Status code: %s".formatted(response.getStatusCode()));
                return Optional.empty();
            }

            assert response.getBody() != null;

            return Optional.of(response.getBody());

        } catch(Exception ex) {
            logger.error("IOException occurred while loading template: %s".formatted(ex.getMessage()));
            return Optional.empty();
        }
    }
}
