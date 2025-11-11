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

    public Optional<String> load(final String assetID, final String templateName) {
        logger.trace("load(assetId=%s, templateName=%s) method called.".formatted(assetID, templateName));

        String targetUri = "/assets/%s/templates/%s".formatted(assetID, templateName);

        ResponseEntity<String> response = template.exchange(targetUri, HttpMethod.GET, null, String.class);

        if (!response.getStatusCode().is2xxSuccessful() || !response.hasBody()) {
            logger.debug("Failed to load template from Assets Registry. Status code: %s".formatted(response.getStatusCode()));
            return Optional.empty();
        }

        return Optional.ofNullable(response.getBody());
    }
}
