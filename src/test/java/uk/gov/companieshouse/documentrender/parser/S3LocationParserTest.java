package uk.gov.companieshouse.documentrender.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static uk.gov.companieshouse.documentrender.config.RestConfig.LOCATION_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.TEMPLATE_NAME_HEADER;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.documentrender.parser.S3LocationParser.S3Location;
import uk.gov.companieshouse.documentrender.utils.HeaderUtils;
import uk.gov.companieshouse.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class S3LocationParserTest {

    @Mock
    Logger logger;

    @InjectMocks
    S3LocationParser underTest;

    @Test
    void givenHeadersWithPDF_whenParseCalled_thenReturnLocation() {
        Map<String, String> headers = HeaderUtils.createValidHeadersForPDF();

        S3Location result = underTest.parse(headers, true);

        assertThat(result, is(notNullValue()));
        assertThat(result.getBucketName(), is("local-test.document-render-service.ch.gov.uk"));
        assertThat(result.getPath(), is("local/company-report/"));
        assertThat(result.getDocumentName(), is(notNullValue()));
    }

    @Test
    void givenInvalidHeadersWithPDF_whenParseCalled_thenReturnLocation() {
        Map<String, String> headers = HeaderUtils.createValidHeadersForPDF();
        headers.put(LOCATION_HEADER, "invalid");

        S3Location result = underTest.parse(headers, true);

        assertThat(result, is(notNullValue()));
        assertThat(result.getBucketName(), is(""));
        assertThat(result.getPath(), is(""));
        assertThat(result.getDocumentName(), is(headers.get(TEMPLATE_NAME_HEADER)));
    }

    @Test
    void givenInvalidHeadersWithFilenamePDF_whenParseCalled_thenReturnLocation() {
        Map<String, String> headers = HeaderUtils.createValidHeadersForPDF();
        headers.put(LOCATION_HEADER, "invalid.pdf");

        S3Location result = underTest.parse(headers, true);

        assertThat(result, is(notNullValue()));
        assertThat(result.getBucketName(), is(""));
        assertThat(result.getPath(), is(""));
        assertThat(result.getDocumentName(), is(headers.get(TEMPLATE_NAME_HEADER)));
    }

    @Test
    void givenInvalidHeadersWithHTML_whenParseCalled_thenReturnLocation() {
        Map<String, String> headers = HeaderUtils.createValidHeadersForHTML();
        headers.put(LOCATION_HEADER, "s3://local-test.document-render-service.ch.gov.uk/local/company-report/report");

        S3Location result = underTest.parse(headers, true);

        assertThat(result, is(notNullValue()));
        assertThat(result.getBucketName(), is("local-test.document-render-service.ch.gov.uk"));
        assertThat(result.getPath(), is("local/company-report/report"));
        assertThat(result.getDocumentName(), is(notNullValue()));
    }


    @Test
    void givenHeadersWithHTML_whenParseCalled_thenReturnLocation() {
        Map<String, String> headers = HeaderUtils.createValidHeadersForHTML();

        S3Location result = underTest.parse(headers, true);

        assertThat(result, is(notNullValue()));
        assertThat(result.getBucketName(), is("local-test.document-render-service.ch.gov.uk"));
        assertThat(result.getPath(), is("local/company-report"));
        assertThat(result.getDocumentName(), is(notNullValue()));
    }
}
