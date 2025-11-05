package uk.gov.companieshouse.documentrender.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class GenerateDocumentServiceTest {

    @Mock
    Logger logger;

    @InjectMocks
    GenerateDocumentService underTest;


    @Test
    void givenTemplateAndData_whenGetCalled_thenAssetReturned() {
        String html = "<html></html>";
        Map<String, String> data = Map.of();

        Optional<Void> result = underTest.render(html, data);

        verify(logger, times(1)).trace("render(html=%s, data=%s) method called.".formatted(html, data));

        assertThat(result, is(notNullValue()));
        assertThat(result.isEmpty(), is(true));
    }

}
