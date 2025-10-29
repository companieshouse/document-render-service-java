package uk.gov.companieshouse.documentrender.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.companieshouse.documentrender.controller.validator.RequireHeaders;
import uk.gov.companieshouse.documentrender.model.Document;
import uk.gov.companieshouse.logging.Logger;

@Controller
@RequestMapping(path = "${spring.service.path.prefix}")
public class DocumentRenderController {

    private final Logger logger;

    public DocumentRenderController(final Logger logger) {
        this.logger = logger;
    }

    @RequireHeaders({"templateName", "assetID", "Accept", "Content-Type", "Location"})
    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity<Void> renderDocument(@RequestBody Document document,
            @RequestParam("is_public") boolean isPublic, @RequestHeader Map<String, String> allHeaders) {
        logger.trace("renderDocument(document=%s, isPublic=%s, headers=%s) method called."
                .formatted(document, isPublic, allHeaders));

        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/store", consumes = "application/json")
    public ResponseEntity<Void> renderAndStoreDocument(@RequestBody Document document,
            @RequestParam("is_public") boolean isPublic, @RequestHeader Map<String, String> allHeaders) {
        logger.trace("renderAndStoreDocument(document=%s, isPublic=%s, headers=%s) method called."
                .formatted(document, isPublic, allHeaders));

        return ResponseEntity.notFound().build();
    }
}
