package uk.gov.companieshouse.documentrender.controller;

import static uk.gov.companieshouse.documentrender.config.RestConfig.ACCEPT_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.ASSET_ID_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.CONTENT_TYPE_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.LOCATION_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.TEMPLATE_NAME_HEADER;

import java.io.InputStream;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.companieshouse.documentrender.controller.validator.RequireHeaders;
import uk.gov.companieshouse.documentrender.model.Document;
import uk.gov.companieshouse.documentrender.processor.DocumentRenderProcessor;
import uk.gov.companieshouse.logging.Logger;

@Controller
@RequestMapping(path = "${spring.service.path.prefix}")
public class DocumentRenderController {

    private final DocumentRenderProcessor processor;
    private final Logger logger;

    public DocumentRenderController(final DocumentRenderProcessor processor, final Logger logger) {
        this.processor = processor;
        this.logger = logger;
    }

    @RequireHeaders({TEMPLATE_NAME_HEADER, ASSET_ID_HEADER, ACCEPT_HEADER, CONTENT_TYPE_HEADER, LOCATION_HEADER})
    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity<byte[]> renderDocument(@RequestBody Document document,
            @RequestParam(value = "is_public", defaultValue = "false") boolean isPublic,
            @RequestHeader Map<String, String> allHeaders) {
        logger.trace("renderDocument(document=%s, isPublic=%s, headers=%s) method called."
                .formatted(document, isPublic, allHeaders));

        byte[] documentContent = processor.render(allHeaders);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .body(documentContent);
    }

    @RequireHeaders({TEMPLATE_NAME_HEADER, ASSET_ID_HEADER, ACCEPT_HEADER, CONTENT_TYPE_HEADER, LOCATION_HEADER})
    @PostMapping(value = "/store", consumes = "application/json")
    public ResponseEntity<byte[]> renderAndStoreDocument(@RequestBody Document document,
            @RequestParam(value = "is_public", defaultValue = "false") boolean isPublic,
            @RequestHeader Map<String, String> allHeaders) {
        logger.trace("renderAndStoreDocument(document=%s, isPublic=%s, headers=%s) method called."
                .formatted(document, isPublic, allHeaders));

        byte[] documentContent = processor.render(allHeaders);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .body(documentContent);
    }
}
