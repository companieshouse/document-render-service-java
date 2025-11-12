package uk.gov.companieshouse.documentrender.processor;

import static uk.gov.companieshouse.documentrender.config.RestConfig.ASSET_ID_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.TEMPLATE_NAME_HEADER;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.documentrender.exception.TemplateNotAvailableException;
import uk.gov.companieshouse.documentrender.model.Document;
import uk.gov.companieshouse.documentrender.parser.S3LocationParser;
import uk.gov.companieshouse.documentrender.parser.S3LocationParser.S3Location;
import uk.gov.companieshouse.documentrender.service.AssetsRegistryService;
import uk.gov.companieshouse.documentrender.service.GenerateDocumentService;
import uk.gov.companieshouse.logging.Logger;

@Component
public class DocumentRenderProcessor {

    private final AssetsRegistryService assetsRegistryService;
    private final GenerateDocumentService generateDocumentService;
    private final S3LocationParser s3LocationParser;
    private final Logger logger;

    public DocumentRenderProcessor(final AssetsRegistryService assetsRegistryService,
            final GenerateDocumentService generateDocumentService,
            final S3LocationParser s3LocationParser,
            final Logger logger) {
        this.assetsRegistryService = assetsRegistryService;
        this.generateDocumentService = generateDocumentService;
        this.s3LocationParser = s3LocationParser;
        this.logger = logger;
    }

    public byte[] render(final HttpHeaders headers, final Document document) {
        logger.trace("render(headers=%s, document=%s) method called.".formatted(headers, document));

        // We need to improve this section, as ERIC modifies the case of some of these headers.
        final Map<String, String> modifiedHeaders = getModifiedHeadersMap(headers);

        // Load the template content from the assets registry.
        String templateContent = loadTemplateContent(modifiedHeaders);
        logger.debug("Template content loaded successfully: %d bytes.".formatted(templateContent.length()));

        return templateContent.getBytes();
    }

    public String store(final HttpHeaders headers, final byte[] data, final Boolean isPublic) {
        logger.trace("store(headers=%s, data=%d, isPublic=%s) method called.".formatted(headers, data.length, isPublic));

        // We need to improve this section, as ERIC modifies the case of some of these headers.
        final Map<String, String> modifiedHeaders = getModifiedHeadersMap(headers);

        String accessControlList = isPublic ? "public-read" : "private";
        logger.debug("Generating document with ACL: %s".formatted(accessControlList));

        S3Location s3Location = s3LocationParser.parse(modifiedHeaders, isPublic);
        logger.debug("Parsed incoming Location: %s".formatted(s3Location));

        String s3Path = "%s/%s/%s".formatted(s3Location.getBucketName(), s3Location.getPath(), s3Location.getDocumentName())
                .replaceAll("/{2,}", "/");;

        return "s3://%s".formatted(s3Path);
    }

    /**
     * Convert HttpHeaders to a case-insensitive map. During development, it was observed that ERIC sometimes
     * modifies the case of headers, causing issues when retrieving them. This method ensures that header retrieval
     * is case-insensitive, and thus more robust against such modifications.
     * @param headers HttpHeaders object containing the request headers.
     * @return A case-insensitive map of headers.
     */
    private Map<String, String> getModifiedHeadersMap(final HttpHeaders headers) {
        logger.trace("getModifiedHeadersMap(headers=%s) method called.".formatted(headers));

        Map<String, String> modifiedHeaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        modifiedHeaders.putAll(headers.toSingleValueMap());

        return modifiedHeaders;
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
