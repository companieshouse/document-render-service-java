package uk.gov.companieshouse.documentrender.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import uk.gov.companieshouse.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class AssetsRegistryServiceTest {

    @Mock
    Logger logger;

    @Mock
    RestTemplate template;

    @InjectMocks
    AssetsRegistryService underTest;

    @Test
    void givenAssetExists_whenLoadCalled_thenContentReturned() {
        String templateName = "letter-template-en-v1.htm";
        String assetId = "letters";

        String targetUrl = "/assets/%s/templates/%s".formatted(assetId, templateName);

        when(template.exchange(targetUrl, HttpMethod.GET, null, String.class))
                .thenReturn(ResponseEntity.status(200).body("Template content"));

        Optional<String> result = underTest.load(assetId, templateName);

        verify(logger, times(1)).trace("load(assetId=%s, templateName=%s) method called.".formatted(assetId, templateName));

        assertThat(result, is(notNullValue()));
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is("Template content"));
    }

    @Test
    void givenAssetExistsButEmpty_whenLoadCalled_thenNoContentReturned() {
        String templateName = "letter-template-en-v1.htm";
        String assetId = "letters";

        String targetUrl = "/assets/%s/templates/%s".formatted(assetId, templateName);

        when(template.exchange(targetUrl, HttpMethod.GET, null, String.class))
                .thenReturn(ResponseEntity.status(200).body(null));

        Optional<String> result = underTest.load(assetId, templateName);

        verify(logger, times(1)).trace("load(assetId=%s, templateName=%s) method called.".formatted(assetId, templateName));

        assertThat(result, is(notNullValue()));
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    void givenAssetNotExists_whenLoadCalled_thenNoContentReturned() {
        String templateName = "letter-template-en-v1.htm";
        String assetId = "letters";

        String targetUrl = "/assets/%s/templates/%s".formatted(assetId, templateName);

        when(template.exchange(targetUrl, HttpMethod.GET, null, String.class))
                .thenReturn(ResponseEntity.status(404).body(null));

        Optional<String> result = underTest.load(assetId, templateName);

        verify(logger, times(1)).trace("load(assetId=%s, templateName=%s) method called.".formatted(assetId, templateName));

        assertThat(result, is(notNullValue()));
        assertThat(result.isEmpty(), is(true));
    }
}
