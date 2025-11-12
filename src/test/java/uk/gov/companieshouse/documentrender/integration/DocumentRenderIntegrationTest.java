package uk.gov.companieshouse.documentrender.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.companieshouse.documentrender.config.RestConfig.TEMPLATE_NAME_HEADER;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.companieshouse.api.error.ApiError;
import uk.gov.companieshouse.api.error.ApiErrorResponse;
import uk.gov.companieshouse.documentrender.utils.DisabledIfDockerUnavailable;
import uk.gov.companieshouse.documentrender.utils.DocumentUtils;
import uk.gov.companieshouse.documentrender.utils.HeaderUtils;

@SpringBootTest(
        webEnvironment = WebEnvironment.DEFINED_PORT,
        properties = {
                "server.port=8042"
        }
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisabledIfDockerUnavailable
public class DocumentRenderIntegrationTest {

    @Value("${spring.service.path.prefix}")
    private String servicePath;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenValidRequest_whenRenderCalled_thenReturnOK() throws Exception {
        var headers = HeaderUtils.createHttpHeadersForPDF();
        var request = DocumentUtils.createValidDocument();

        String jsonBody = mapper.writeValueAsString(request);

        mockMvc.perform(
                        post("%s/".formatted(servicePath))
                                .headers(headers)
                                .header("X-Request-Id", "my-x-request-id")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBody)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
        ;
    }

    @Test
    public void givenMissingHeaders_whenRenderCalled_thenReturnBadRequest() throws Exception {
        var headerMap = HeaderUtils.createValidHeaderMapForPDF();
        headerMap.remove(TEMPLATE_NAME_HEADER);

        var request = DocumentUtils.createValidDocument();

        ApiError apiError = new ApiError();
        apiError.setError("An expected header ws not supplied with the request: Required header 'templateName' is missing");
        apiError.setLocation("handleMissingHeaderException");
        apiError.setLocationType("request-header");
        apiError.setType("document-render");

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setErrors(List.of(apiError));

        var headers = new HttpHeaders(headerMap);

        String jsonBody = mapper.writeValueAsString(request);
        String jsonError = mapper.writeValueAsString(apiErrorResponse);

        mockMvc.perform(
                        post("%s/".formatted(servicePath))
                                .headers(headers)
                                .header("X-Request-Id", "my-x-request-id")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBody)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonError));
    }

    @Test
    public void givenBlankHeader_whenRenderCalled_thenReturnBadRequest() throws Exception {
        var headerMap = HeaderUtils.createValidHeaderMapForPDF();
        headerMap.set(TEMPLATE_NAME_HEADER, "");

        var request = DocumentUtils.createValidDocument();

        ApiError apiError = new ApiError();
        apiError.setError("An expected header ws not supplied with the request: Required header 'templateName' is missing");
        apiError.setLocation("handleMissingHeaderException");
        apiError.setLocationType("request-header");
        apiError.setType("document-render");

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setErrors(List.of(apiError));

        var headers = new HttpHeaders(headerMap);

        String jsonBody = mapper.writeValueAsString(request);
        String jsonError = mapper.writeValueAsString(apiErrorResponse);

        mockMvc.perform(
                        post("%s/".formatted(servicePath))
                                .headers(headers)
                                .header("X-Request-Id", "my-x-request-id")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBody)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonError));
//                .andExpect(jsonPath("$.errors").value(jsonError))
//                .andExpect(jsonPath("$.description").value("A test item"))
        ;
    }

    @Test
    public void givenValidRequestPDF_whenStoreCalled_thenReturnOK() throws Exception {
        var headers = HeaderUtils.createHttpHeadersForPDF();
        var request = DocumentUtils.createValidDocument();

        String jsonBody = mapper.writeValueAsString(request);

        mockMvc.perform(
                        post("%s/store".formatted(servicePath))
                                .headers(headers)
                                .header("X-Request-Id", "my-x-request-id")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBody)
                                .accept(MediaType.APPLICATION_PDF)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(header().exists("Location"))
        ;
    }

    @Test
    public void givenValidRequestHTML_whenStoreCalled_thenReturnOK() throws Exception {
        var headers = HeaderUtils.createHttpHeadersForHTML();
        var request = DocumentUtils.createValidDocument();

        String jsonBody = mapper.writeValueAsString(request);

        mockMvc.perform(
                        post("%s/store".formatted(servicePath))
                                .headers(headers)
                                .header("X-Request-Id", "my-x-request-id")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBody)
                                .accept(MediaType.TEXT_HTML)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(header().exists("Location"))
        ;
    }

    @Test
    public void givenInvalidRequest_whenStoreCalled_thenReturnOK() throws Exception {
        var headers = HeaderUtils.createHttpHeadersForPDF();
        var request = DocumentUtils.createValidDocument();

        String jsonBody = mapper.writeValueAsString(request);

        mockMvc.perform(
                        post("%s/store".formatted(servicePath))
                                .headers(headers)
                                .header("X-Request-Id", "my-x-request-id")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBody)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(header().doesNotExist("Location"))

        ;
    }

}
