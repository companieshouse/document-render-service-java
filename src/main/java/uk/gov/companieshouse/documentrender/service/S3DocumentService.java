package uk.gov.companieshouse.documentrender.service;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.logging.Logger;

@Service
public class S3DocumentService {

    private final Logger logger;

    public S3DocumentService(final Logger logger) {
        this.logger = logger;
    }

}
