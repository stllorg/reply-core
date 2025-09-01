package org.stll.reply.core.e2e.users;

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

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserE2ETest {

    private static final String USERNAME = "u_e2e_" + UUID.randomUUID().toString().substring(0, 8);
    private static final String EMAIL = USERNAME + "@test.com";
    private static final String PASSWORD = "TestPassword123!";

    private static String jwtToken;
    private static String userId;

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
    public void testGetAuthenticatedUserAndRoles() {
        if (jwtToken == null) {
            throw new IllegalStateException("JWT token not available. Login test might have failed.");
        }

        Response response = given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .when()
                .get("/auth/authenticate");

        response.then()
                .statusCode(200)
                .body("userId", notNullValue())
                .body("roles", hasItem("user"));

        if (userId == null) {
            userId = response.jsonPath().getString("userId");
        }
    }

    @Test
    @Order(4)
    public void testGetUserById() {
        if (userId == null) {
            throw new IllegalStateException("User ID not available. Registration or authentication test might have failed.");
        }

        given()
                .when()
                .get("/users/" + userId)
                .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("username", equalTo(USERNAME))
                .body("email", equalTo(EMAIL));
    }

}