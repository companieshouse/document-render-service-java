package uk.gov.companieshouse.documentrender.parser;

import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.TEXT_HTML;
import static uk.gov.companieshouse.documentrender.config.RestConfig.ACCEPT_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.LOCATION_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.TEMPLATE_NAME_HEADER;
import static uk.gov.companieshouse.documentrender.utils.S3PathUtils.hasFileExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.documentrender.exception.UnsupportedMimeTypeException;
import uk.gov.companieshouse.logging.Logger;

@Component
public class S3LocationParser {

    private static final Pattern LOCATION_REGEX = Pattern.compile("^s3://([a-zA-Z0-9\\-_.]+)/(.*)$");
    //private static final Pattern LOCATION_WITH_FILENAME_REGEX = Pattern.compile("^s3://([a-zA-Z0-9\\-_.]+)/(.+)/(.+\\..+)$");

    /**
     * We have a number of Sonar PR issues around this regular expression, but they're mitigated elsewhere in the code.
     * The length of the filename is checked elsewhere, and the bucket name and path are controlled inputs.
     */
    @SuppressWarnings({"java:S5852","java:S5998"})
    private static final Pattern LOCATION_WITH_FILENAME_REGEX =
            Pattern.compile(
                    "^s3://"
                            + "([A-Za-z0-9\\-\\.]+)"                // group 1: bucket name
                            + "(/(?:[A-Za-z0-9\\-_/\\.]+/)*)"       // group 2: folder path (starting and ending with slash)
                            + "(?:([A-Za-z0-9\\-_.]+\\.[A-Za-z0-9]+))?$"  // group 3: optional filename with extension
            );

    private static final List<MediaType> SUPPORTED_MEDIA_TYPES = List.of(
            APPLICATION_PDF, TEXT_HTML
    );

    private final Logger logger;

    public S3LocationParser(final Logger logger) {
        this.logger = logger;
    }

    public S3Location parse(final Map<String, String> headers, final Boolean isPublic) {
        logger.trace("parse(headers=%s, public=%s) method called.".formatted(headers, isPublic));

        MediaType mediaType = getMimeType(headers.get(ACCEPT_HEADER));
        return parseStoreLocation(headers, mediaType);
    }

    private MediaType getMimeType(final String accept) {
        logger.trace("getMimeType(accept=%s) method called.".formatted(accept));

        // Parse and validate the Accept header, to determine the requested media type.
        MediaType acceptMediaType = MediaType.parseMediaType(accept);

        // Check if the requested media type is supported.
        if(!SUPPORTED_MEDIA_TYPES.contains(acceptMediaType)) {
            logger.info("Unsupported mime type, error will be thrown: '%s'" + accept);
            throw new UnsupportedMimeTypeException("Unsupported Mime Type for rendering: '%s'".formatted(accept));
        }

        return acceptMediaType;
    }

    /**
     * Parse the S3 location from the (Location) headers and mime type. Examples of a location are:
     * - s3://s3-bucket.mock.bucket-name/path/company-report/
     * - s3://s3-bucket.mock.bucket-name/path/company-report/filename.html
     * @return S3Location object containing bucket name, path, and document name.
     */
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
                String extension = mimeType.equals(APPLICATION_PDF) ? ".pdf" : ".html";
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
