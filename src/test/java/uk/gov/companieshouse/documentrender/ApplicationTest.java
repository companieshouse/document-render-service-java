package uk.gov.companieshouse.documentrender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        webEnvironment = WebEnvironment.DEFINED_PORT,
        properties = {
                "server.port=9191"
        }
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ApplicationTest {

    @Test
    void mainShouldRunWithoutErrors() {
        Application.main(new String[]{});
    }

}