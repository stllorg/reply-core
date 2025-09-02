package org.stll.reply.core.Controllers;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.stll.reply.core.Entities.User;
import org.stll.reply.core.Services.UserService;
import org.stll.reply.core.dtos.*;
import org.stll.reply.core.utils.RolesConverter;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@JBossLog
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    RolesConverter rolesConverter;

    @Inject
    JsonWebToken jwt;

    // CREATE user
    @POST
    @RolesAllowed("admin")
    public Response createUser(@Valid RegistrationRequest request) {

        log.info("UserResource Received email {}" + request.getEmail());

        try {
            User user = userService.createUserFromRequest(request.getUsername(), request.getEmail(), request.getPassword());

            UserDTO userResponse = new UserDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail()
            );

            return Response.created(URI.create("/users/" + userResponse.getId())).entity(userResponse).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // GET user
    @GET
    @RolesAllowed({"admin", "user"})
    @Path("/{id}")
    public Response getUserById(@PathParam("id") UUID id) {

        // Check if user is admin
        Set<String> roles = jwt.getGroups();
        String userIdString = jwt.getClaim("id").toString();
        UUID userId = UUID.fromString(userIdString);

        if (roles.contains("admin")) {
            log.info("AuthResource The admin with id " + userId + "requested to delete user with id " + id);
        } else {
            if (userId.equals(id)) {
                log.info("AuthResource The user with id " + userId + " requested to get account info.");
            } else {
                log.info("AuthResource The user with id " + userId + " has no admin rights to fetch thirdy user data.");
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        return userService.findUserById(id)
                .map(user -> {
                    UserDTO userResponse = new UserDTO(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail()
                    );

                    return Response.ok(userResponse).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    // DELETE User by id
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"admin", "user"})
    public Response deleteUserById(@PathParam("id") UUID id) {
        // Check if user is admin
        Set<String> roles = jwt.getGroups();
        String userIdString = jwt.getClaim("id").toString();
        UUID userId = UUID.fromString(userIdString);

        if (roles.contains("admin")) {
            log.info("AuthResource The admin with id " + userId + "requested to delete user with id " + id);
        } else {
            if (userId.equals(id)) {
            log.info("AuthResource The user with id " + userId + " requestes to delete the account.");
            } else {
                log.info("AuthResource The user with id " + userId + " has no admin rights to delete thirdy user accounts.");
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }

        boolean isDeleted = userService.delete(id);
        if (isDeleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // Update user password
    @PUT
    @Path("/{id}/password")
    @RolesAllowed("user")
    @Transactional
    public Response updateUserPassword(@PathParam("id") UUID id, UpdateUserPasswordRequest request) {
        String userIdString = jwt.getClaim("id").toString();
        UUID userId = UUID.fromString(userIdString);

        if (userId.equals(id)) {
            log.info("AuthResource The user with id " + userId + " has requested to update the password.");
        } else {
            log.info("AuthResource The user with id " + userId + " is forbidden to manage thirdy user's data.");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        log.info("AuthResource UpdateUserPassword - Searching user by id.");

        Optional<User> userOptional = userService.findUserById(id);

        if (userOptional.isPresent()) {
            User userToUpdate = userOptional.get();

            log.info("AuthResource UpdateUserPassword - Found user with id:" + userToUpdate.getId());

            userToUpdate.setPassword(request.password);

            // Update user
            return userService.update(userToUpdate)
                    .map(user -> Response.noContent().build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // Update user email
    @PUT
    @Path("/{id}/email")
    @RolesAllowed("user")
    @Transactional
    public Response updateUserEmail(@PathParam("id") UUID id, UpdateUserEmailRequest request) {

        String userIdString = jwt.getClaim("id").toString();
        UUID userId = UUID.fromString(userIdString);

        if (userId.equals(id)) {
            log.info("AuthResource The user with id " + userId + " has requested to update the email.");
        } else {
            log.info("AuthResource The user with id " + userId + " is forbidden to manage thirdy user's data.");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        log.info("AuthResource UpdateUserEmail - Searching user by id.");

        Optional<User> userOptional = userService.findUserById(id);

        if (userOptional.isPresent()) {
            User userToUpdate = userOptional.get();

            log.info("AuthResource UpdateUserEmail - Found user with id:" + userToUpdate.getId());

            userToUpdate.setPassword(request.email);

            // Update user
            return userService.update(userToUpdate)
                    .map(user -> Response.noContent().build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // Update username
    @PUT
    @Path("/{id}/username")
    @RolesAllowed("user")
    @Transactional
    public Response updateUserUsername(@PathParam("id") UUID id, UpdateUserUsernameRequest request) {

        String userIdString = jwt.getClaim("id").toString();
        UUID userId = UUID.fromString(userIdString);

        if (userId.equals(id)) {
            log.info("AuthResource The user with id " + userId + " has requested to update the username.");
        } else {
            log.info("AuthResource The user with id " + userId + " is forbidden to manage thirdy user's data.");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        log.info("AuthResource UpdateUserUsername - Searching user by id.");

        Optional<User> userOptional = userService.findUserById(id);

        if (userOptional.isPresent()) {
            User userToUpdate = userOptional.get();

            log.info("AuthResource UpdateUserUsername - Found user with id:" + userToUpdate.getId());

            userToUpdate.setPassword(request.username);

            // Update user
            return userService.update(userToUpdate)
                    .map(user -> Response.noContent().build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // GET ALL users
    @GET
    @RolesAllowed("admin")
    public Response fetchUsers(@QueryParam("page") @DefaultValue("1") int page,
                               @QueryParam("limit") @DefaultValue("15") int limit) {
        PaginationResponse<User> usersResult = userService.getUsers(page, limit);
        if (usersResult.getData().isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity(Collections.singletonMap("error", "Users not found")).build();
        }
        return Response.ok(usersResult).build();
    }

    // GET Roles by User id
    @GET
    @Path("/{targetId}/roles")
    @RolesAllowed({"admin", "user"})
    public Response fetchUserRoles(@PathParam("targetId") UUID targetId) {

        // Check if user is admin
        Set<String> roles = jwt.getGroups();
        String userIdString = jwt.getClaim("id").toString();
        UUID userId = UUID.fromString(userIdString);

        log.info("Current user " + userId + "\n target user:" + targetId);

        if (roles.contains("admin")) {
            log.info("AuthResource The admin with id " + userId + "requested to fetch roles of user with id " + targetId);
        } else {
            if (userId.equals(targetId)) {
                log.info("AuthResource The user with id " + userId + " requestes to fetch account data of roles.");
            } else {
                log.info("AuthResource The user with id " + userId + " has no admin rights to fetch thirdy user accounts.");
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(Collections.singletonMap("error", "You do not have permission to access this resource.")).build();
            }
        }

        Optional<User> targetUser = userService.findUserById(targetId);
        if (targetUser.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity(Collections.singletonMap("error", "User not found")).build();
        }

        List<String> targetUserRoles = List.of(userService.getUserRolesByUserId(targetId));
        return Response.ok(Collections.singletonMap("roles", targetUserRoles)).build();
    }

    // Update User Role by User Id
    @PUT
    @Path("/{targetId}/roles")
    @RolesAllowed("admin")
    public Response updateUserRole(@PathParam("targetId") UUID targetId, RoleUpdateRequest request) {
        if (request.getRoleNames() == null || request.getRoleNames().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonMap("error", "Missing roles value")).build();
        }
        try {
            Optional<User> targetUser = userService.findUserById(targetId);
            if (targetUser.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity(Collections.singletonMap("error", "User not found")).build();
            }

            // Convert request.roles from names to roleIds
            List<Integer> roleIdsList = rolesConverter.execute(request.getRoleNames());

            userService.updateUserRoles(targetId, roleIdsList);

            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonMap("error", "Failed to update user roles")).build();
        }
    }

    @PUT
    @Path("/me/upgrade/roles")
    @RolesAllowed("user")
    public Response upgradeUserRoles() {
        // Check current user id
        Set<String> roles = jwt.getGroups();
        String currentUserIdString = jwt.getClaim("id").toString();
        UUID currentUserId = UUID.fromString(currentUserIdString);

        try {
            Optional<User> targetUser = userService.findUserById(currentUserId);
            if (targetUser.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity(Collections.singletonMap("error", "User not found")).build();
            }

            List<String> requiredRoles = List.of("admin", "manager", "support");

            List<String> newRolesToAdd = requiredRoles.stream()
                    .filter(role -> !roles.contains(role))
                    .collect(Collectors.toList());

            if (newRolesToAdd.isEmpty()) {
                return Response.status(Response.Status.CONFLICT).entity(Collections.singletonMap("error", "The user has all roles")).build();
            }

            // Convert roles from names to ids
            List<Integer> roleIdsList = rolesConverter.execute(newRolesToAdd);

            userService.updateUserRoles(currentUserId, roleIdsList);

            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonMap("error", "Failed to update user roles")).build();
        }
    }

}
