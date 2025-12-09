package uk.gov.companieshouse.documentrender.controller.validator;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.documentrender.exception.MissingHeaderException;

@Aspect
@Component
public class RequireHeadersAspect {

    private final HttpServletRequest request;

    public RequireHeadersAspect(final HttpServletRequest request) {
        this.request = request;
    }

    @Before("@annotation(requireHeaders)")
    public void checkHeaders(final JoinPoint jp, final RequireHeaders requireHeaders) {

        // Iterate through the required headers and check if they are present in the request
        for (String header : requireHeaders.value()) {
            String value = request.getHeader(header);
            if (value == null || value.isBlank()) {
                throw new MissingHeaderException("Required header '" + header + "' is missing");
            }
        }

    }
}