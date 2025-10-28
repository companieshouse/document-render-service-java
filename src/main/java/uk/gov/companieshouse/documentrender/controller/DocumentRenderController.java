package uk.gov.companieshouse.documentrender.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.companieshouse.documentrender.model.Document;
import uk.gov.companieshouse.logging.Logger;

@Controller
@RequestMapping(path = "${spring.service.path.prefix}")
public class DocumentRenderController {

    private final Logger logger;

    public DocumentRenderController(final Logger logger) {
        this.logger = logger;
    }

    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity<Void> renderDocument(@RequestBody Document document, @RequestParam("is_public") boolean isPublic) {
        logger.trace("renderDocument(document=%s, isPublic=%s) method called.".formatted(document, isPublic));

        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/store", consumes = "application/json")
    public ResponseEntity<Void> renderAndStoreDocument(@RequestBody Document document, @RequestParam("is_public") boolean isPublic) {
        logger.trace("renderAndStoreDocument(document=%s, isPublic=%s) method called.".formatted(document, isPublic));

        return ResponseEntity.notFound().build();
    }
}
