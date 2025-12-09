package uk.gov.companieshouse.documentrender.utils;

import static uk.gov.companieshouse.documentrender.config.RestConfig.ACCEPT_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.ASSET_ID_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.CONTENT_TYPE_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.LOCATION_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.TEMPLATE_NAME_HEADER;

import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class HeaderUtils {

    public static HttpHeaders createHttpHeadersForPDF() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(createValidHeadersForPDF());
        return httpHeaders;
    }

    public static HttpHeaders createHttpHeadersForHTML() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(createValidHeadersForHTML());
        return httpHeaders;
    }

    public static Map<String, String> createValidHeadersForPDF() {
        return createValidHeaderMapForPDF().toSingleValueMap();
    }

    public static Map<String, String> createValidHeadersForHTML() {
        return createValidHeaderMapForHTML().toSingleValueMap();
    }

    public static MultiValueMap<String, String> createValidHeaderMapForPDF() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(ASSET_ID_HEADER, "letters");
        headers.add(TEMPLATE_NAME_HEADER, "letter-template-en-v1.htm");
        headers.add(ACCEPT_HEADER, "application/pdf");
        headers.add(CONTENT_TYPE_HEADER, "application/json");
        headers.add(LOCATION_HEADER, "s3://local-test.document-render-service.ch.gov.uk/local/company-report/");
        return headers;
    }

    public static MultiValueMap<String, String> createValidHeaderMapForHTML() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(ASSET_ID_HEADER, "letters");
        headers.add(TEMPLATE_NAME_HEADER, "letter-template-en-v1.htm");
        headers.add(ACCEPT_HEADER, "text/html");
        headers.add(CONTENT_TYPE_HEADER, "application/json");
        headers.add(LOCATION_HEADER, "s3://local-test.document-render-service.ch.gov.uk/local/company-report/report.html");
        return headers;
    }
}
