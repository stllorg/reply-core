package org.stll.reply.core.Controllers;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.stll.reply.core.Entities.User;
import org.stll.reply.core.Services.UserService;
import org.stll.reply.core.dtos.PaginationResponse;
import org.stll.reply.core.dtos.RegistrationRequest;
import org.stll.reply.core.dtos.RoleUpdateRequest;
import org.stll.reply.core.dtos.UserDTO;
import org.stll.reply.core.utils.RolesConverter;

import java.util.*;

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
    @PermitAll
    public Response createUser(@Valid RegistrationRequest request) {

        log.info("UserResource Received email {}" + request.getEmail());

        try {
            User user = userService.createUserFromRequest(request.getUsername(), request.getEmail(), request.getPassword());

            UserDTO userResponse = new UserDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail()
            );

            return Response.ok(userResponse).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // GET user
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") UUID id) {
        return userService.findUserById(id)
                .map(ticket -> Response.ok(ticket).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    // DELETE User by Id
    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response deleteUserById(@PathParam("id") UUID id) {
        boolean isDeleted = userService.delete(id);
        if (isDeleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
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

    // GET Roles by User Id
    @GET
    @Path("/{targetId}/roles")
    @RolesAllowed({"admin", "user"})
    public Response fetchUserRoles(@PathParam("targetId") UUID targetId) {
        String currentUserIdString = jwt.getClaim("id").toString();
        UUID currentUserId = null;

        try {
            currentUserId = UUID.fromString(currentUserIdString);
        } catch (IllegalArgumentException e) {
            log.error("Failed to convert the user id from JWT claim to UUID:");
            currentUserId = null;
        }

        if (currentUserId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An auth token with user id is required").build();
        }

        Set<String> currentUserRoles = jwt.getGroups();
        if (currentUserId != targetId && !currentUserRoles.contains("admin")) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Collections.singletonMap("error", "You do not have permission to access this resource.")).build();
        }
        Optional<User> targetUser = userService.findUserById(targetId);
        if (targetUser.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity(Collections.singletonMap("error", "User not found")).build();
        }
        List<String> roles = List.of(userService.getUserRolesByUserId(targetId));
        return Response.ok(Collections.singletonMap("roles", roles)).build();
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

            // TODO: Update user roles
            userService.updateUserRoles(targetId, roleIdsList);

            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonMap("error", "Failed to update user roles")).build();
        }
    }
}
