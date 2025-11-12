package uk.gov.companieshouse.documentrender.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class S3PathUtilsTest {

    @Test
    void givenPathWithExtension_whenGetFileExtensionCalled_thenReturnExtension() {
        String s3Path = "s3://my-bucket/path/to/my-document.html";

        Optional<String> result = S3PathUtils.getFileExtension(s3Path);

        assertThat(result, is(notNullValue()));
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is("html"));
    }

    @Test
    void givenPathWithoutExtension_whenGetFileExtensionCalled_thenReturnEmpty() {
        String s3Path = "s3://my-bucket/path/to/my-document";

        Optional<String> result = S3PathUtils.getFileExtension(s3Path);

        assertThat(result, is(notNullValue()));
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    void givenNullPath_whenGetFileExtensionCalled_thenReturnEmpty() {
        Optional<String> result = S3PathUtils.getFileExtension(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    void givenEmptyPath_whenGetFileExtensionCalled_thenReturnEmpty() {
        String s3Path = "";

        Optional<String> result = S3PathUtils.getFileExtension(s3Path);

        assertThat(result, is(notNullValue()));
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    void givenBasePath_whenGetFileExtensionCalled_thenReturnEmpty() {
        String s3Path = "/";

        Optional<String> result = S3PathUtils.getFileExtension(s3Path);

        assertThat(result, is(notNullValue()));
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    void givenBasicPath_whenGetFileExtensionCalled_thenReturnEmpty() {
        String s3Path = "/document.";

        Optional<String> result = S3PathUtils.getFileExtension(s3Path);

        assertThat(result, is(notNullValue()));
        assertThat(result.isEmpty(), is(true));
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
