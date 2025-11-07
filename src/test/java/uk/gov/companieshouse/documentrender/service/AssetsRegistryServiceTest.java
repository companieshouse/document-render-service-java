package uk.gov.companieshouse.documentrender.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    void givenAssetExists_whenGetCalled_thenAssetReturned() {
        String templateName = "letter-template-en-v1.htm";
        String assetId = "letters";

        when(template.getForEntity(eq("/assets/{assetID}/templates/{templateName}"),
                eq(ByteArrayInputStream.class), eq(assetId), eq(templateName)))
                .thenReturn(ResponseEntity.status(200).body(new ByteArrayInputStream("Template content".getBytes())));

        Optional<String> result = underTest.load(templateName, assetId);

        verify(logger, times(1)).trace("load(templateName=%s, assetId=%s) method called.".formatted(templateName, assetId));

        assertThat(result, is(notNullValue()));
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is("Template content"));
    }
}
