package uk.gov.companieshouse.documentrender.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.gov.companieshouse.api.error.ApiError;
import uk.gov.companieshouse.api.error.ApiErrorResponse;
import uk.gov.companieshouse.documentrender.model.Document;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DocumentRenderIntegrationTest {

    @Value("${spring.service.path.prefix}")
    private String servicePath;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenValidRequest_whenRenderDocumentCalled_thenReturnOK() throws Exception {
        var document = new Document();

        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.add("templateName", "1");
        headerMap.add("assetId", "1");
        headerMap.add("Accept", "application/json");
        headerMap.add("Content-Type", "application/json");
        headerMap.add("Location", "s3-bucket");

        var headers = new HttpHeaders(headerMap);

        String jsonBody = mapper.writeValueAsString(document);

        mockMvc.perform(
                        post("%s/".formatted(servicePath))
                                .headers(headers)
                                .header("X-Request-Id", "my-x-request-id")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBody)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void givenValidRequest_whenRenderAndStoreDocumentCalled_thenReturnOK() throws Exception {
        var document = new Document();

        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.add("templateName", "1");
        headerMap.add("assetId", "1");
        headerMap.add("Accept", "application/json");
        headerMap.add("Content-Type", "application/json");
        headerMap.add("Location", "s3-bucket");

        var headers = new HttpHeaders(headerMap);

        String jsonBody = mapper.writeValueAsString(document);

        mockMvc.perform(
                        post("%s/store".formatted(servicePath))
                                .headers(headers)
                                .header("X-Request-Id", "my-x-request-id")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBody)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void givenMissingHeaders_whenRenderDocumentCalled_thenReturnBadRequest() throws Exception {
        var document = new Document();

        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.add("assetId", "1");
        headerMap.add("Accept", "application/json");
        headerMap.add("Content-Type", "application/json");
        headerMap.add("Location", "s3-bucket");

        ApiError apiError = new ApiError();
        apiError.setError("An expected header ws not supplied with the request: Required header 'templateName' is missing");
        apiError.setLocation("handleMissingHeaderException");
        apiError.setLocationType("request-header");
        apiError.setType("document-render");

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setErrors(List.of(apiError));

        var headers = new HttpHeaders(headerMap);

        String jsonBody = mapper.writeValueAsString(document);
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
    public void givenBlankHeader_whenRenderDocumentCalled_thenReturnBadRequest() throws Exception {
        var document = new Document();

        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.add("templateName", "");
        headerMap.add("assetId", "1");
        headerMap.add("Accept", "application/json");
        headerMap.add("Content-Type", "application/json");
        headerMap.add("Location", "s3-bucket");

        ApiError apiError = new ApiError();
        apiError.setError("An expected header ws not supplied with the request: Required header 'templateName' is missing");
        apiError.setLocation("handleMissingHeaderException");
        apiError.setLocationType("request-header");
        apiError.setType("document-render");

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setErrors(List.of(apiError));

        var headers = new HttpHeaders(headerMap);

        String jsonBody = mapper.writeValueAsString(document);
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
}
