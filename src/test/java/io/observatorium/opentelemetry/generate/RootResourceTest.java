package io.observatorium.opentelemetry.generate;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class RootResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/order")
          .then()
             .statusCode(200)
             .body(is("Created"));
    }

}