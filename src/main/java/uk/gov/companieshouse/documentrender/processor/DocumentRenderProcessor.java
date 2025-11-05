package uk.gov.companieshouse.documentrender.processor;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.documentrender.service.AssetsRegistryService;
import uk.gov.companieshouse.documentrender.service.GenerateDocumentService;
import uk.gov.companieshouse.logging.Logger;

@Component
public class DocumentRenderProcessor {

    private final AssetsRegistryService assetsRegistryService;
    private final GenerateDocumentService generateDocumentService;
    private final Logger logger;

    public DocumentRenderProcessor(final AssetsRegistryService assetsRegistryService,
            final GenerateDocumentService generateDocumentService,
            final Logger logger) {
        this.assetsRegistryService = assetsRegistryService;
        this.generateDocumentService = generateDocumentService;
        this.logger = logger;
    }

    public Resource render() {
        logger.trace("render() method called.");

        byte[] documentBytes = "This is my document content.".getBytes();

        return new InputStreamResource(new BufferedInputStream(new ByteArrayInputStream(documentBytes)));
    }
}
