package uk.gov.companieshouse.documentrender.exception;

public class MissingHeaderException extends RuntimeException {

    public MissingHeaderException(final String message) {
        super(message);
    }

}
