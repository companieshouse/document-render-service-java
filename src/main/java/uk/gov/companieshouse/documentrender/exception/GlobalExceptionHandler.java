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

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(final BadRequestException e) {
        logger.error("An %s was raised during processing!".formatted(e.getClass().getSimpleName()), e);

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
        logger.error("An %s was raised during processing!".formatted(e.getClass().getSimpleName()), e);

        return ErrorResponseBuilder
                .status(HttpStatus.NOT_FOUND)
                .withError("HTML5 template was not found in assets registry.",
                        "handleNotFoundException",
                        "method",
                        "document-render")
                .build();
    }

}
