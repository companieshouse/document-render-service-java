package uk.gov.companieshouse.documentrender.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import uk.gov.companieshouse.documentrender.model.Document;
import uk.gov.companieshouse.documentrender.processor.DocumentRenderProcessor;
import uk.gov.companieshouse.documentrender.utils.DocumentUtils;
import uk.gov.companieshouse.documentrender.utils.HeaderUtils;
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
        HttpHeaders headers = HeaderUtils.createHttpHeadersForPDF();
        Document document = DocumentUtils.createValidDocument();
        boolean isPublic = true;

        ResponseEntity<byte[]> response = underTest.render(headers, document, isPublic);

        verify(documentRenderProcessor, times(1)).render(headers, document);

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(201));
    }

    @Test
    void givenValidPrivateRequest_whenRenderDocumentCalled_thenOkReturned() {
        HttpHeaders headers = HeaderUtils.createHttpHeadersForPDF();
        Document document = DocumentUtils.createValidDocument();
        boolean isPublic = false;

        ResponseEntity<byte[]> response = underTest.render(headers, document, isPublic);

        verify(documentRenderProcessor, times(1)).render(headers, document);

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(201));
    }

    @Test
    void givenValidPublicRequest_whenStoreDocumentCalled_thenOkReturned() {
        byte[] documentContent = "<html><body>Test Document</body></html>".getBytes();
        String s3Location = URI.create("s3://bucket/path/document.html").toString();

        HttpHeaders headers = HeaderUtils.createHttpHeadersForPDF();
        Document document = DocumentUtils.createValidDocument();
        boolean isPublic = true;

        when(documentRenderProcessor.render(headers, document)).thenReturn(documentContent);
        when(documentRenderProcessor.store(headers, documentContent, isPublic)).thenReturn(s3Location);

        ResponseEntity<byte[]> response = underTest.store(headers, document, isPublic);

        verify(documentRenderProcessor, times(1)).render(headers, document);
        verify(documentRenderProcessor, times(1)).store(headers, documentContent, isPublic);

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(201));
    }

    @Test
    void givenValidPrivateRequest_whenStoreDocumentCalled_thenOkReturned() {
        byte[] documentContent = "<html><body>Test Document</body></html>".getBytes();
        String s3Location = URI.create("s3://bucket/path/document.html").toString();

        HttpHeaders headers = HeaderUtils.createHttpHeadersForPDF();
        Document document = DocumentUtils.createValidDocument();
        boolean isPublic = false;

        when(documentRenderProcessor.render(headers, document)).thenReturn(documentContent);
        when(documentRenderProcessor.store(headers, documentContent, isPublic)).thenReturn(s3Location);

        ResponseEntity<byte[]> response = underTest.store(headers, document, isPublic);

        verify(documentRenderProcessor, times(1)).render(headers, document);
        verify(documentRenderProcessor, times(1)).store(headers, documentContent, isPublic);

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(201));
    }
}
