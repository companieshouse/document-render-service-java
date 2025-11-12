package uk.gov.companieshouse.documentrender.controller;

import static uk.gov.companieshouse.documentrender.config.RestConfig.ACCEPT_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.ASSET_ID_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.CONTENT_TYPE_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.LOCATION_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.TEMPLATE_NAME_HEADER;

import java.net.URI;
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
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> render(@RequestHeader HttpHeaders headers, @RequestBody Document document,
            @RequestParam(value = "is_public", defaultValue = "false") boolean isPublic) {
        logger.trace("render(document=%s, isPublic=%s) method called.".formatted(document, isPublic));

        byte[] content = processor.render(headers, document);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(responseHeaders)
                .body(content);
    }

    @RequireHeaders({TEMPLATE_NAME_HEADER, ASSET_ID_HEADER, ACCEPT_HEADER, CONTENT_TYPE_HEADER, LOCATION_HEADER})
    @PostMapping(value = "/store", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> store(@RequestHeader HttpHeaders headers, @RequestBody Document document,
            @RequestParam(value = "is_public", defaultValue = "false") boolean isPublic) {
        logger.trace("store(document=%s, isPublic=%s) method called.".formatted(document, isPublic));

        byte[] content = processor.render(headers, document);
        String location = processor.store(headers, content, isPublic);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeaders.setLocation(URI.create(location));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(responseHeaders)
                .body(content);
    }
}
