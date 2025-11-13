package uk.gov.companieshouse.documentrender.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class S3PathUtilsTest {

    @ParameterizedTest(name = "{index} => path=''{0}'', expectedExt={1}, present={2}")
    @CsvSource({
            "s3://my-bucket/path/to/my-document.html, html, true",
            "s3://my-bucket/path/to/my-document, , false",
            "null, , false",
            " , , false",
            "/, , false",
            "/document., , false"
    })
    void givenVariousPaths_whenGetFileExtension_thenReturnResult(String s3Path, String expectedExt, boolean present) {
        Optional<String> result = S3PathUtils.getFileExtension(s3Path);

        assertThat(result, is(notNullValue()));
        assertThat(result.isPresent(), is(present));

        if(result.isPresent() && present) {
            assertThat(result.get(), is(expectedExt));
        }
    }

    @Test
    void givenPathWithExtensions_whenHasFileExtensionCalled_thenReturnTrue() {
        String s3Path = "s3://my-bucket/path/to/my-document.html";

        boolean result = S3PathUtils.hasFileExtension(s3Path);

        assertThat(result, is(true));
    }

    @Test
    void givenPathWithoutExtension_whenHasFileExtensionCalled_thenReturnFalse() {
        String s3Path = "s3://my-bucket/path/to/my-document";

        boolean result = S3PathUtils.hasFileExtension(s3Path);

        assertThat(result, is(false));
    }

}
