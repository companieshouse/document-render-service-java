package uk.gov.companieshouse.documentrender.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.companieshouse.api.error.ApiError;
import uk.gov.companieshouse.api.error.ApiErrorResponse;
import uk.gov.companieshouse.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @Mock
    Logger logger;

    GlobalExceptionHandler underTest;

    @BeforeEach
    void setUp() {
        underTest = new GlobalExceptionHandler(logger);
    }

    @Test
    void givenErrorRaised_whenErrorResponseCreated_thenReturnApiErrorResponseBuilder() {
        ResponseEntity<ApiErrorResponse> response = ErrorResponseBuilder
                .status(HttpStatus.NOT_FOUND)
                .withError("error message", "location", "locationType", "type")
                .build();

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(404));
    }

    @Test
    void givenNonErrorRaised_whenErrorResponseCreated_thenReturnApiErrorResponseBuilder() {
        IllegalArgumentException expectedException = assertThrows(IllegalArgumentException.class, () -> {
            ErrorResponseBuilder.status(HttpStatus.OK);
        });

        assertThat(expectedException, is(notNullValue()));
        assertThat(expectedException.getMessage(), is("Status to ApiErrorResponse must be an error status. 200 (200 OK) is not."));
    }

    @Test
    void givenBadRequest_whenExceptionRaised_thenReturnApiErrorResponse() {
        ResponseEntity<ApiErrorResponse> response = underTest.handleBadRequestException(new BadRequestException());

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(400));

        ApiErrorResponse body = response.getBody();
        assertThat(body, is(notNullValue()));
        assertThat(body.getErrors().size(), is(1));

        ApiError error = body.getErrors().getFirst();
        assertThat(error.getError(), is("Unable to render document due to a bad request."));
        assertThat(error.getLocation(), is("handleBadRequestException"));
        assertThat(error.getType(), is("document-render"));
        assertThat(error.getLocationType(), is("method"));
        assertThat(error.getErrorValues(), is(nullValue()));
    }

    @Test
    void givenTemplateNotFound_whenExceptionRaised_thenReturnApiErrorResponse() {
        ResponseEntity<ApiErrorResponse> response = underTest.handleNotFoundException(new NotFoundException());

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(404));

        ApiErrorResponse body = response.getBody();
        assertThat(body, is(notNullValue()));
        assertThat(body.getErrors().size(), is(1));

        ApiError error = body.getErrors().getFirst();
        assertThat(error.getError(), is("HTML5 template was not found in assets registry."));
        assertThat(error.getLocation(), is("handleNotFoundException"));
        assertThat(error.getType(), is("document-render"));
        assertThat(error.getLocationType(), is("method"));
        assertThat(error.getErrorValues(), is(nullValue()));
    }
}
