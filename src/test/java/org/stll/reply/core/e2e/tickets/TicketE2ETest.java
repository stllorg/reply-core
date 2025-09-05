package org.stll.reply.core.e2e.tickets;

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

    private static final String USERNAME = "t_e2e_" + UUID.randomUUID().toString().substring(0, 8);
    private static final String EMAIL = USERNAME + "@test.com";
    private static final String PASSWORD = "TestPassword123!";
    private static final String TICKET_SUBJECT = "Ticket for E2E test.";
    private static final String UPDATED_TICKET_SUBJECT = "Updated ticket subject for E2E test.";

    private static String jwtToken;
    private static String userId;
    private static String createdTicketId;

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
        if (userId == null) {
            userId = response.jsonPath().getString("userId");
        }

        createdTicketId = response.jsonPath().getString("id");
    }

    @Test
    @Order(4)
    public void testTicketUpdate() {
        if (createdTicketId == null) {
            throw new IllegalStateException("Ticket ID not available. Ticket creation test might have failed.");
        }

        String updatePayload = "{\"subject\":\"" + UPDATED_TICKET_SUBJECT + "\"}";

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(updatePayload)
                .when()
                .put("/tickets/" + createdTicketId)
                .then()
                .statusCode(200)
                .body("id", equalTo(createdTicketId))
                .body("subject", equalTo(UPDATED_TICKET_SUBJECT));
    }

    @Test
    @Order(5)
    public void testFetchTicketById() {
        if (createdTicketId == null) {
            throw new IllegalStateException("Ticket ID not available. Ticket creation test might have failed.");
        }

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/tickets/" + createdTicketId)
                .then()
                .statusCode(200)
                .body("id", equalTo(createdTicketId))
                .body("subject", equalTo(UPDATED_TICKET_SUBJECT));
    }

    @Test
    @Order(6)
    public void testArchiveTicket() {
        if (createdTicketId == null) {
            throw new IllegalStateException("Ticket ID not available. Ticket creation test might have failed.");
        }

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .put("/tickets/" + createdTicketId + "/archive")
                .then()
                .statusCode(200)
                .body("id", equalTo(createdTicketId))
                .body("status", equalTo("closed"));
    }

    @Test
    @Order(7)
    public void testFetchAllTicketsByUserId() {
        if (userId == null) {
            throw new IllegalStateException("User ID not available. Registration or authentication test might have failed.");
        }

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .when()
                .get("/tickets/user/" + userId + "?limit=15&page=1")
                .then()
                .statusCode(200)
                .body("data", not(empty()))
                .body("data.size()", greaterThan(0))
                .body("totalItems", greaterThan(0))
                .body("data[0].subject", notNullValue())
                .body("data[0].status", notNullValue());
    }

    @Test
    @Order(8)
    public void testDeleteTicketAsNonAdminFails() {
        if (createdTicketId == null) {
            throw new IllegalStateException("Ticket ID not available. Ticket creation test might have failed.");
        }

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken) // Use the user's token
                .when()
                .delete("/tickets/" + createdTicketId)
                .then()
                .statusCode(403); // Expecting a Forbidden status code
    }
}