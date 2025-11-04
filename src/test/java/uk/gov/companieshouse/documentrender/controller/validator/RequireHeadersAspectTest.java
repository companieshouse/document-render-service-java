package uk.gov.companieshouse.documentrender.controller.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.documentrender.exception.MissingHeaderException;

@ExtendWith(MockitoExtension.class)
public class RequireHeadersAspectTest {

    @Mock
    HttpServletRequest request;

    @InjectMocks
    RequireHeadersAspect underTest;

    @BeforeEach
    public void setUp() {
        // Nothing to do yet.
    }

    @Test
    public void givenValidHeader_whenAspectCalled_thenNoExceptionsRaised() {
        String headerName = "MyHeaderName";
        String headerValue = "MyHeaderValue";

        JoinPoint mockJoinPoint = mock(JoinPoint.class);
        RequireHeaders mockHeaders = mock(RequireHeaders.class);

        when(mockHeaders.value()).thenReturn(new String[]{headerName});
        when(request.getHeader(headerName)).thenReturn(headerValue);

        underTest.checkHeaders(mockJoinPoint, mockHeaders);

        verify(request, times(1)).getHeader(headerName);
    }

    @Test
    public void givenBlankHeaderValue_whenAspectCalled_thenNoExceptionsRaised() {
        String headerName = "MyHeaderName";
        String headerValue = "   ";

        JoinPoint mockJoinPoint = mock(JoinPoint.class);
        RequireHeaders mockHeaders = mock(RequireHeaders.class);

        when(mockHeaders.value()).thenReturn(new String[]{headerName});
        when(request.getHeader(headerName)).thenReturn(headerValue);

        MissingHeaderException expectedException = assertThrows(MissingHeaderException.class, () -> {
            underTest.checkHeaders(mockJoinPoint, mockHeaders);
        });

        verify(request, times(1)).getHeader(headerName);

        assertThat(expectedException, is(notNullValue()));
        assertThat(expectedException.getMessage(), is("Required header '%s' is missing".formatted(headerName)));
    }

    @Test
    public void givenEmptyHeaderValue_whenAspectCalled_thenNoExceptionsRaised() {
        String headerName = "MyHeaderName";
        String headerValue = "";

        JoinPoint mockJoinPoint = mock(JoinPoint.class);
        RequireHeaders mockHeaders = mock(RequireHeaders.class);

        when(mockHeaders.value()).thenReturn(new String[]{headerName});
        when(request.getHeader(headerName)).thenReturn(headerValue);

        MissingHeaderException expectedException = assertThrows(MissingHeaderException.class, () -> {
            underTest.checkHeaders(mockJoinPoint, mockHeaders);
        });

        verify(request, times(1)).getHeader(headerName);

        assertThat(expectedException, is(notNullValue()));
        assertThat(expectedException.getMessage(), is("Required header '%s' is missing".formatted(headerName)));
    }


    @Test
    public void givenNullHeaderValue_whenAspectCalled_thenNoExceptionsRaised() {
        String headerName = "MyHeaderName";
        String headerValue = null;

        JoinPoint mockJoinPoint = mock(JoinPoint.class);
        RequireHeaders mockHeaders = mock(RequireHeaders.class);

        when(mockHeaders.value()).thenReturn(new String[]{headerName});
        when(request.getHeader(headerName)).thenReturn(headerValue);

        MissingHeaderException expectedException = assertThrows(MissingHeaderException.class, () -> {
            underTest.checkHeaders(mockJoinPoint, mockHeaders);
        });

        verify(request, times(1)).getHeader(headerName);

        assertThat(expectedException, is(notNullValue()));
        assertThat(expectedException.getMessage(), is("Required header '%s' is missing".formatted(headerName)));
    }
}
