package org.stll.reply.core.e2e.messages;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MessageE2ETest {

    private static final String USERNAME = "m_e2e_" + UUID.randomUUID().toString().substring(0, 8);
    private static final String EMAIL = USERNAME + "@test.com";
    private static final String PASSWORD = "TestPassword123!";
    private static final String TICKET_SUBJECT = "The Ticket for E2E test.";
    private static final String MESSAGE_CONTENT = "First message in a new ticket.";
    private static final String UPDATED_MESSAGE_CONTENT = "This is the updated message content.";

    private static String jwtToken;
    private static String createdTicketId;
    private static String createdMessageId;

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
                .statusCode(201)
                .body("subject", equalTo(TICKET_SUBJECT))
                .body("id", notNullValue())
                .body("userId", notNullValue());

        createdTicketId = response.jsonPath().getString("id");
    }

    @Test
    @Order(4)
    public void testCreateMessage() {
        if (jwtToken == null) {
            throw new IllegalStateException("JWT token not available. Login test might have failed.");
        }

        if (createdTicketId == null) {
            throw new IllegalStateException("Ticket id was not found. Ticket creation tests might have failed.");
        }

        String messagePayload = "{\"message\":\"" + MESSAGE_CONTENT + "\"}";

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(messagePayload)
                .when()
                .post("/messages/" + createdTicketId);

        response.then()
                .statusCode(201)
                .body("message", equalTo(MESSAGE_CONTENT))
                .body("id", notNullValue())
                .body("userId", notNullValue())
                .body("ticketId", equalTo(createdTicketId));

        createdMessageId = response.jsonPath().getString("id");
    }

    @Test
    @Order(5)
    public void testUpdateMessage() {
        if (jwtToken == null) {
            throw new IllegalStateException("JWT token not available. Login test might have failed.");
        }

        if (createdTicketId == null) {
            throw new IllegalStateException("Ticket id was not found. Ticket creation tests might have failed.");
        }

        String updatePayload = "{\"message\":\"" + UPDATED_MESSAGE_CONTENT + "\"}";

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(updatePayload)
                .when()
                .put("/messages/" + createdMessageId)
                .then()
                .statusCode(200)
                .body("id", equalTo(createdMessageId))
                .body("message", equalTo(UPDATED_MESSAGE_CONTENT));
    }

    @Test
    @Order(6)
    public void testGetMessageById() {
        if (jwtToken == null) {
            throw new IllegalStateException("JWT token not available. Login test might have failed.");
        }

        if (createdTicketId == null) {
            throw new IllegalStateException("Ticket id was not found. Ticket creation tests might have failed.");
        }

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/messages/" + createdMessageId)
                .then()
                .statusCode(200)
                .body("id", equalTo(createdMessageId))
                .body("message", equalTo(UPDATED_MESSAGE_CONTENT));
    }

    @Test
    @Order(7)
    public void testFetchAllMessagesByTicketId() {
        if (jwtToken == null) {
            throw new IllegalStateException("JWT token not available. Login test might have failed.");
        }

        if (createdTicketId == null) {
            throw new IllegalStateException("Ticket id was not found. Ticket creation tests might have failed.");
        }
        
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .when()
                .get("/messages/ticket/" + createdTicketId + "?limit=15&page=1")
                .then()
                .statusCode(200)
                .body("data", not(empty()))
                .body("data.size()", greaterThan(0))
                .body("totalItems", greaterThan(0))
                .body("data[0].id", notNullValue())
                .body("data[0].message", notNullValue());
    }

    @Test
    @Order(8)
    public void testDeleteMessage() {
        if (jwtToken == null) {
            throw new IllegalStateException("JWT token not available. Login test might have failed.");
        }

        if (createdTicketId == null) {
            throw new IllegalStateException("Ticket id was not found. Ticket creation tests might have failed.");
        }

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .delete("/messages/" + createdMessageId)
                .then()
                .statusCode(204);
    }
}