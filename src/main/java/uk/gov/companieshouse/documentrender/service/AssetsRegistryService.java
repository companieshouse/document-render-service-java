package uk.gov.companieshouse.documentrender.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.logging.Logger;

@Service
public class AssetsRegistryService {

    private final Logger logger;

    public AssetsRegistryService(final Logger logger) {
        this.logger = logger;
    }

    public Optional<Void> load(final String templateName, final String assetID) {
        logger.trace("load(templateName=%s, assetId=%s) method called.".formatted(templateName, assetID));

        return Optional.empty();
    }
}
