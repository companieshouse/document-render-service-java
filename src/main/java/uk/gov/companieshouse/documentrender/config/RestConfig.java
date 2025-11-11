package uk.gov.companieshouse.documentrender.config;

import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import uk.gov.companieshouse.logging.Logger;

@Configuration
public class RestConfig {

    public static final String TEMPLATE_NAME_HEADER = "templateName";
    public static final String ASSET_ID_HEADER = "assetID";
    public static final String ACCEPT_HEADER = "Accept";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String LOCATION_HEADER = "Location";

    /**
     * ResourceHttpMessageConverter resourceConverter = new ResourceHttpMessageConverter();
     * resourceConverter.setSupportedMediaTypes(
     *      Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL)
     * );
     */
    @Bean(name = "assetsRegistryRestTemplate")
    RestTemplate assetsRegistryRestTemplate(@Value("${spring.internal.asset-registry.url}") String assetsRegistryUrl, Logger logger) {
        logger.trace("assetsRegistryRestTemplate(url=%s) method called.".formatted(assetsRegistryUrl));

        return restTemplateBuilder()
                .rootUri(assetsRegistryUrl)
                .messageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
                .build();
    }

    RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

}
