package uk.gov.companieshouse.documentrender.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import uk.gov.companieshouse.documentrender.model.Document;
import uk.gov.companieshouse.documentrender.processor.DocumentRenderProcessor;
import uk.gov.companieshouse.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class DocumentRenderControllerTest {

    @Mock
    DocumentRenderProcessor documentRenderProcessor;

    @Mock
    Logger logger;

    DocumentRenderController underTest;

    @BeforeEach
    void setUp() {
        underTest = new DocumentRenderController(documentRenderProcessor, logger);
    }

    @Test
    void givenValidPublicRequest_whenRenderDocumentCalled_thenOkReturned() {
        Map<String, String> headers = new HashMap<>();
        headers.put("templateName", "1");
        headers.put("assetId", "1");
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Location", "s3-bucket");

        Document document = new Document();

        ResponseEntity<Resource> response = underTest.renderDocument(document, true, headers);

        verify(documentRenderProcessor, times(1)).render();

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(201));
    }

    @Test
    void givenValidPrivateRequest_whenRenderDocumentCalled_thenOkReturned() {
        Map<String, String> headers = new HashMap<>();
        headers.put("templateName", "1");
        headers.put("assetId", "1");
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Location", "s3-bucket");

        Document document = new Document();

        ResponseEntity<Resource> response = underTest.renderDocument(document, false, headers);

        verify(documentRenderProcessor, times(1)).render();

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(201));
    }

    @Test
    void givenValidPublicRequest_whenRenderAndStoreDocumentCalled_thenOkReturned() {
        Map<String, String> headers = new HashMap<>();
        headers.put("templateName", "1");
        headers.put("assetId", "1");
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Location", "s3-bucket");

        Document document = new Document();

        ResponseEntity<Resource> response = underTest.renderAndStoreDocument(document, true, headers);

        verify(documentRenderProcessor, times(1)).render();

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(201));
    }

    @Test
    void givenValidPrivateRequest_whenRenderAndStoreDocumentCalled_thenOkReturned() {
        Map<String, String> headers = new HashMap<>();
        headers.put("templateName", "1");
        headers.put("assetId", "1");
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Location", "s3-bucket");

        Document document = new Document();

        ResponseEntity<Resource> response = underTest.renderAndStoreDocument(document, false, headers);

        verify(documentRenderProcessor, times(1)).render();

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(201));
    }
}
