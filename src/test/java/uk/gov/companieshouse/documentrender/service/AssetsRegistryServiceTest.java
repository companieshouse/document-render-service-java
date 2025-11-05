package uk.gov.companieshouse.documentrender.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class AssetsRegistryServiceTest {

    @Mock
    Logger logger;

    @InjectMocks
    AssetsRegistryService underTest;

    @Test
    void givenAssetExists_whenGetCalled_thenAssetReturned() {
        String templateName = "dissolution";
        String assetId = "asset123";

        Optional<Void> result = underTest.load(templateName, assetId);

        verify(logger, times(1)).trace("load(templateName=%s, assetId=%s) method called.".formatted(templateName, assetId));

        assertThat(result, is(notNullValue()));
        assertThat(result.isEmpty(), is(true));
    }
}
