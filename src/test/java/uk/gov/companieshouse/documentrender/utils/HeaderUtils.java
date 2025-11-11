package uk.gov.companieshouse.documentrender.utils;

import static uk.gov.companieshouse.documentrender.config.RestConfig.ACCEPT_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.ASSET_ID_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.CONTENT_TYPE_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.LOCATION_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.TEMPLATE_NAME_HEADER;

import java.util.Map;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class HeaderUtils {

    public static Map<String, String> createValidHeaders() {
        return createValidHeaderMap().toSingleValueMap();
    }

    public static MultiValueMap<String, String> createValidHeaderMap() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(ASSET_ID_HEADER, "letters");
        headers.add(TEMPLATE_NAME_HEADER, "letter-template-en-v1.htm");
        headers.add(ACCEPT_HEADER, "application/json");
        headers.add(CONTENT_TYPE_HEADER, "application/json");
        headers.add(LOCATION_HEADER, "s3-bucket");
        return headers;
    }
}
