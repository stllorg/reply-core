package org.stll.reply.core;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import java.util.UUID;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketE2ETest {

    private static final String USERNAME = "testuser_e2e_" + UUID.randomUUID().toString().substring(0, 8);
    private static final String EMAIL = USERNAME + "@test.com";
    private static final String PASSWORD = "TestPassword123!";
    private static final String TICKET_SUBJECT = "Assunto do meu ticket de teste E2E.";

    private static String jwtToken;

    @Test
    @Order(1)
    public void testUserRegistration() {
        String registrationPayload = "{\"username\":\"" + USERNAME + "\", \"email\":\"" + EMAIL + "\", \"password\":\"" + PASSWORD + "\"}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(registrationPayload)
                .when()
                .post("/auth/register");

        response.then()
                .statusCode(201)
                .body("username", equalTo(USERNAME))
                .body("email", equalTo(EMAIL));
    }

    @Test
    @Order(2)
    public void testUserLoginAndGetToken() {
        String loginPayload = "{\"username\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginPayload)
                .when()
                .post("/auth/login");

        response.then()
                .statusCode(200)
                .body("message", equalTo("Login successful"))
                .body("token", notNullValue());

        jwtToken = response.jsonPath().getString("token");
    }

    @Test
    @Order(3)
    public void testTicketCreationWithToken() {
        if (jwtToken == null) {
            throw new IllegalStateException("JWT token not available. Login test might have failed.");
        }

        String ticketPayload = "{\"subject\":\"" + TICKET_SUBJECT + "\"}";

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(ticketPayload)
                .when()
                .post("/tickets");

        response.then()
                .statusCode(200)
                .body("subject", equalTo(TICKET_SUBJECT))
                .body("id", notNullValue())
                .body("userId", notNullValue());
    }
}