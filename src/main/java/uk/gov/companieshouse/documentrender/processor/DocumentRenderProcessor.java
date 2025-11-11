package uk.gov.companieshouse.documentrender.processor;

import static uk.gov.companieshouse.documentrender.config.RestConfig.ASSET_ID_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.TEMPLATE_NAME_HEADER;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.documentrender.exception.TemplateNotAvailableException;
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

    public byte[] render(final Map<String, String> requestHeaders) {
        logger.trace("render(headers=%s) method called.".formatted(requestHeaders));

        // We need to improve this section, as ERIC modifies the case of some of these headers.
        Map<String, String> allHeaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        allHeaders.putAll(requestHeaders);

        String templateContent = loadTemplateContent(allHeaders);
        logger.debug("Template content loaded successfully: %d bytes.".formatted(templateContent.length()));

        return templateContent.getBytes();
    }

    private String loadTemplateContent(final Map<String, String> headers) {
        logger.trace("loadTemplateContent(headers=%s) method called.".formatted(headers));

        String assetID = headers.get(ASSET_ID_HEADER);
        String templateName = headers.get(TEMPLATE_NAME_HEADER);

        logger.debug("Loading template content for: (AssetID=%s, TemplateName=%s)".formatted(assetID, templateName));
        Optional<String> template = assetsRegistryService.load(assetID, templateName);

        if(template.isEmpty()) {
            logger.debug("WARNING: Template not found for name: %s and asset ID: %s".formatted(templateName, assetID));
            throw new TemplateNotAvailableException("Template not found for name: " + templateName + " and asset ID: " + assetID);
        }

        return template.get();
    }
}
