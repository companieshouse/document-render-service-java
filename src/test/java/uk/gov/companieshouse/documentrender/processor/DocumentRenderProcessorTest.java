package uk.gov.companieshouse.documentrender.processor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.documentrender.config.RestConfig.ASSET_ID_HEADER;
import static uk.gov.companieshouse.documentrender.config.RestConfig.TEMPLATE_NAME_HEADER;

import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.documentrender.exception.TemplateNotAvailableException;
import uk.gov.companieshouse.documentrender.service.AssetsRegistryService;
import uk.gov.companieshouse.documentrender.service.GenerateDocumentService;
import uk.gov.companieshouse.documentrender.utils.HeaderUtils;
import uk.gov.companieshouse.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class DocumentRenderProcessorTest {

    @Mock
    AssetsRegistryService assetsRegistryService;

    @Mock
    GenerateDocumentService generateDocumentService;

    @Mock
    Logger logger;

    @InjectMocks
    DocumentRenderProcessor underTest;

    @Test
    void givenAssetExists_whenRenderCalled_thenReturnTemplateContent() {
        String assetContent = "<html>Document</html>";

        Map<String, String> headers = HeaderUtils.createValidHeaders();
        String assetID = headers.get(ASSET_ID_HEADER);
        String templateName = headers.get(TEMPLATE_NAME_HEADER);

        when(assetsRegistryService.load(assetID, templateName)).thenReturn(Optional.of(assetContent));

        byte[] result = underTest.render(headers);

        verify(logger, times(2)).trace(anyString());
        verify(assetsRegistryService, times(1)).load(assetID, templateName);

        verifyNoInteractions(generateDocumentService);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(assetContent.getBytes()));
        assertThat(result.length, is(assetContent.getBytes().length));
    }

    @Test
    void givenAssetNotExists_whenRenderCalled_thenRaiseException() {
        String assetContent = null;

        Map<String, String> headers = HeaderUtils.createValidHeaders();
        String assetID = headers.get(ASSET_ID_HEADER);
        String templateName = headers.get(TEMPLATE_NAME_HEADER);

        when(assetsRegistryService.load(assetID, templateName)).thenReturn(Optional.ofNullable(assetContent));

        TemplateNotAvailableException expectedException = assertThrows(TemplateNotAvailableException.class, () -> {
            underTest.render(headers);
        });

        verify(logger, times(2)).trace(anyString());
        verify(assetsRegistryService, times(1)).load(assetID, templateName);

        verifyNoInteractions(generateDocumentService);

        assertThat(expectedException, is(notNullValue()));
        assertThat(expectedException.getMessage(), is("Template not found for name: %s and asset ID: %s".formatted(templateName, assetID)));
    }
}
