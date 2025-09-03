package org.stll.reply.core.e2e.users;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserAdminE2ETest {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "Senha1234";

    private static final String NEW_USER_USERNAME = "test_" + UUID.randomUUID().toString().substring(0, 8);
    private static final String NEW_USER_EMAIL = NEW_USER_USERNAME + "@test.com";
    private static final String NEW_USER_PASSWORD = "Password123!";

    private static String jwtToken;
    private static String userId;
    private static String createdUserId;

    @Test
    @Order(1)
    public void testUserAdminLoginAndGetToken() {
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
    @Order(2)
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
                .body("roles", hasItem("admin"));

        if (userId == null) {
            userId = response.jsonPath().getString("userId");
        }
    }

    @Test
    @Order(3)
    public void testGetAllUsers() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .when()
                .get("/users?limit=15&page=1")
                .then()
                .statusCode(200)
                .body("data", not(empty()))
                .body("data.size()", greaterThan(0))
                .body("totalItems", greaterThan(0))
                .body("data[0].username", notNullValue())
                .body("data[0].email", notNullValue());
    }

    @Test
    @Order(4)
    public void testCreateNewUser() {
        String newUserPayload = String.format("{\"username\":\"%s\", \"email\":\"%s\", \"password\":\"%s\"}",
                NEW_USER_USERNAME, NEW_USER_EMAIL, NEW_USER_PASSWORD);

        Response response = given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(newUserPayload)
                .when()
                .post("/users");

        response.then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("username", equalTo(NEW_USER_USERNAME))
                .body("email", equalTo(NEW_USER_EMAIL));

        createdUserId = response.jsonPath().getString("id");
    }

    @Test
    @Order(5)
    public void testUpdateUserRoles() {
        if (createdUserId == null) {
            throw new IllegalStateException("Created user ID is not available. Create user test might have failed.");
        }

        List<String> updatedRoles = List.of("manager", "support", "user");
        Map<String, List<String>> updateRolePayload = new HashMap<>();
        updateRolePayload.put("roleNames", updatedRoles);

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(updateRolePayload)
                .when()
                .put("/users/" + createdUserId + "/roles")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    public void testGetUserRolesAfterUpdate() {
        if (createdUserId == null) {
            throw new IllegalStateException("Created user ID is not available. Create user test might have failed.");
        }

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .when()
                .get("/users/" + createdUserId + "/roles")
                .then()
                .statusCode(200)
                .body("roles", hasItems("manager", "support", "user"))
                .body("roles.size()", equalTo(3));
    }

}