package uk.gov.companieshouse.documentrender.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.gov.companieshouse.api.error.ApiErrorResponse;
import uk.gov.companieshouse.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger;

    @Autowired
    public GlobalExceptionHandler(final Logger logger) {
        this.logger = logger;
    }

    @ExceptionHandler({MissingHeaderException.class})
    public ResponseEntity<ApiErrorResponse> handleMissingHeaderException(final MissingHeaderException e) {
        logger.error("An MissingHeaderException was raised during processing!", e);

        return ErrorResponseBuilder
                .status(HttpStatus.BAD_REQUEST)
                .withError("An expected header ws not supplied with the request: %s".formatted(e.getMessage()),
                        "handleMissingHeaderException",
                        "request-header",
                        "document-render")
                .build();
    }

    @ExceptionHandler({TemplateNotAvailableException.class})
    public ResponseEntity<ApiErrorResponse> handleTemplateNotAvailableException(final TemplateNotAvailableException e) {
        logger.error("An TemplateNotAvailableException was raised during processing!", e);

        return ErrorResponseBuilder
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .withError("An assets registry exception occurred: %s".formatted(e.getMessage()),
                        "handleTemplateNotAvailableException",
                        "assets-registry",
                        "get-template")
                .build();
    }


    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(final BadRequestException e) {
        logger.error("An BadRequestException was raised during processing!", e);

        return ErrorResponseBuilder
                .status(HttpStatus.BAD_REQUEST)
                .withError("Unable to render document due to a bad request.",
                        "handleBadRequestException",
                        "method",
                        "document-render")
                .build();
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(final NotFoundException e) {
        logger.error("An NotFoundException was raised during processing!", e);

        return ErrorResponseBuilder
                .status(HttpStatus.NOT_FOUND)
                .withError("HTML5 template was not found in assets registry.",
                        "handleNotFoundException",
                        "method",
                        "document-render")
                .build();
    }

}
