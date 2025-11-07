package uk.gov.companieshouse.documentrender.processor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import uk.gov.companieshouse.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class DocumentRenderProcessorTest {

    @Mock
    Logger logger;

    @InjectMocks
    DocumentRenderProcessor underTest;

    @Test
    void shouldRenderDocument() throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("templateName", "1");
        headers.put("assetId", "1");
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Location", "s3-bucket");

        Resource result = underTest.render(headers);

        verify(logger, times(1)).trace("render() method called.");

        assertThat(result, is(notNullValue()));
        assertThat(result.contentLength(), is(28L));
    }
}
