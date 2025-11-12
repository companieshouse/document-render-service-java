package uk.gov.companieshouse.documentrender.parser;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static uk.gov.companieshouse.documentrender.config.RestConfig.ACCEPT_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.CONTENT_TYPE_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.LOCATION_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.TEMPLATE_NAME_HEADER;
import static uk.gov.companieshouse.documentrender.utils.S3PathUtils.hasFileExtension;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.logging.Logger;

@Component
public class S3LocationParser {

    private static final Pattern LOCATION_REGEX = Pattern.compile("^s3://([a-zA-Z0-9\\-_.]+)/(.*)$");
    private static final Pattern LOCATION_WITH_FILENAME_REGEX = Pattern.compile("^s3://([a-zA-Z0-9\\-_.]+)/(.+)/(.+\\..+)$");

    private final Logger logger;

    public S3LocationParser(final Logger logger) {
        this.logger = logger;
    }

    // s3://development-eu-west-2.document-render-service.ch.gov.uk/cidev/company-report/
    // s3://development-eu-west-2.document-render-service.ch.gov.uk/cidev/company-report/filename.html
    public S3Location parse(final Map<String, String> headers, final Boolean isPublic) {
        logger.trace("parse(headers=%s, public=%s) method called.".formatted(headers, isPublic));

        String contentType = headers.get(CONTENT_TYPE_HEADER);
        String accept = headers.get(ACCEPT_HEADER);

        MediaType mediaType = getMimeType(contentType, accept);

        return parseStoreLocation(headers, mediaType);
    }

    private MediaType getMimeType(final String contentType, final String accept) {
        logger.trace("getMimeType(contentType=%s, accept=%s) method called.".formatted(contentType, accept));

        if(contentType.equals(TEXT_HTML_VALUE) && accept.equals(APPLICATION_PDF_VALUE)) {
            return MediaType.APPLICATION_PDF;

        } else if(contentType.equals(TEXT_HTML_VALUE) && accept.equals(TEXT_HTML_VALUE)) {
            return MediaType.TEXT_HTML;

        } else {
            logger.info("Unsupported mime type, error will be thrown: %s" + contentType);
            //throw new UnsupportedMimeTypeException("Unsupported rendering options: (Content-Type=%s, Accept=%s)".formatted(contentType, accept));
        }

        // TODO: We need to remove this defaulting when we have proper error handling in place.
        return MediaType.TEXT_HTML;
    }

    private S3Location parseStoreLocation(final Map<String, String> headers, final MediaType mimeType) {
        logger.trace("parseStoreLocation(headers=%s, mimeType=%s) method called.".formatted(headers, mimeType));

        final String templateName = headers.get(TEMPLATE_NAME_HEADER);
        final String s3Path = headers.get(LOCATION_HEADER);

        String bucketName = "";
        String path = "";
        String documentName = templateName;

        if(hasFileExtension(s3Path)) {
            var matcher = LOCATION_WITH_FILENAME_REGEX.matcher(s3Path);
            if(matcher.matches()) {
                bucketName = matcher.group(1);
                path = matcher.group(2);
                documentName = matcher.group(3);
            }

        } else {
            var matcher = LOCATION_REGEX.matcher(s3Path);
            if(matcher.matches()) {
                String extension = mimeType.equals(MediaType.APPLICATION_PDF) ? ".pdf" : ".html";
                String uuid = UUID.randomUUID().toString();


                bucketName = matcher.group(1);
                path = matcher.group(2);
                documentName = "document-" + uuid + extension;
            }
        }

        return new S3Location(bucketName, path, documentName);
    }

    public static class S3Location {
        private final String bucketName;
        private final String path;
        private final String documentName;

        public S3Location(String bucketName, String path, String documentName) {
            this.bucketName = bucketName;
            this.path = path;
            this.documentName = documentName;
        }

        public String getBucketName() {
            return bucketName;
        }

        public String getPath() {
            return path;
        }

        public String getDocumentName() {
            return documentName;
        }

        @Override
        public String toString() {
            return "S3Location[bucketName=" + bucketName + ", path=" + path + ", documentName=" + documentName + "]";
        }

    }
}
