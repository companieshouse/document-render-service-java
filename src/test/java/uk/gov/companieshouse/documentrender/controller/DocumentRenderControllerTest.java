package uk.gov.companieshouse.documentrender.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import uk.gov.companieshouse.documentrender.model.Document;
import uk.gov.companieshouse.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class DocumentRenderControllerTest {

    @Mock
    Logger logger;

    DocumentRenderController underTest;

    @BeforeEach
    void setUp() {
        underTest = new DocumentRenderController(logger);
    }

    @Test
    void givenValidPublicRequest_whenRenderDocumentCalled_thenNotFoundReturned() {
        Map<String, String> headers = new HashMap<>();
        headers.put("templateName", "1");
        headers.put("assetId", "1");
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Location", "s3-bucket");

        Document document = new Document();

        ResponseEntity<Void> response = underTest.renderDocument(document, true, headers);

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(404));
    }

    @Test
    void givenValidPrivateRequest_whenRenderDocumentCalled_thenNotFoundReturned() {
        Map<String, String> headers = new HashMap<>();
        headers.put("templateName", "1");
        headers.put("assetId", "1");
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Location", "s3-bucket");

        Document document = new Document();

        ResponseEntity<Void> response = underTest.renderDocument(document, false, headers);

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(404));
    }

    @Test
    void givenValidPublicRequest_whenRenderAndStoreDocumentCalled_thenNotFoundReturned() {
        Map<String, String> headers = new HashMap<>();
        headers.put("templateName", "1");
        headers.put("assetId", "1");
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Location", "s3-bucket");

        Document document = new Document();

        ResponseEntity<Void> response = underTest.renderAndStoreDocument(document, true, headers);

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(404));
    }

    @Test
    void givenValidPrivateRequest_whenRenderAndStoreDocumentCalled_thenNotFoundReturned() {
        Map<String, String> headers = new HashMap<>();
        headers.put("templateName", "1");
        headers.put("assetId", "1");
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Location", "s3-bucket");

        Document document = new Document();

        ResponseEntity<Void> response = underTest.renderAndStoreDocument(document, false, headers);

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(404));
    }
}
