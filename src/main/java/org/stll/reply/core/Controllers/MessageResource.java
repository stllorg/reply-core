package org.stll.reply.core.Controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.stll.reply.core.Entities.Message;
import org.stll.reply.core.Services.MessageService;
import org.stll.reply.core.dtos.CreateMessageRequest;
import org.stll.reply.core.dtos.SaveMessageResponse;
import org.stll.reply.core.dtos.UpdateMessageRequest;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@JBossLog
public class MessageResource {

    @Inject
    MessageService messageService;

    @Inject
    JsonWebToken jwt;

    // CREATE new message inside a ticket
    @POST
    @Path("/{ticketId}")
    @RolesAllowed({"user", "admin", "support", "manager"})
    public Response createMessage(@PathParam("ticketId") UUID ticketId, CreateMessageRequest request) {

        log.info("MessageResource Received token for validation");
        String userIdString = jwt.getClaim("id").toString();

        UUID userId = null;

        try {
            userId = UUID.fromString(userIdString);
        } catch (IllegalArgumentException e) {
            log.error("Failed to convert the user id String from JWT claim to UUID:");
        }

        if (userId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User Id (UUID) is required").build();
        }

        if (ticketId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Ticket Id is required").build();
        }

        if (request == null || request.message == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Message content is missing.").build();
        }

        try {
            Message createdMessage = messageService.createMessage(ticketId, userId, request.message);
            return Response.created(URI.create("/messages/" + createdMessage.getId())).entity(createdMessage).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // UPDATE message
    @PUT
    @Path("/{id}")
    public Response updateMessage(@PathParam("id") UUID id, UpdateMessageRequest request) {
        Optional<Message> messageOptional = messageService.findMessageById(id);

        if (messageOptional.isPresent()) {
            Message messageToUpdate = messageOptional.get();
            messageToUpdate.setMessage(request.message);

           return messageService.updateMessage(messageToUpdate)
                    .map(updatedMessage -> {

                        SaveMessageResponse dto = new SaveMessageResponse(
                                updatedMessage.getId(),
                                updatedMessage.getMessage(),
                                updatedMessage.getCreatedAt()
                        );

                        return Response.ok(dto).build();
                            }
                    )
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());

        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // GET message by Id
    @GET
    @Path("/{id}")
    public Response getMessageById(@PathParam("id") UUID id) {
        return messageService.findMessageById(id)
                .map(ticket -> Response.ok(ticket).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    // GET Messages BY Ticket Id
    @GET
    @Path("/ticket/{ticketId}")
    public Response getMessagesByTicketId(@PathParam("ticketId") UUID ticketId) {
        List<Message> messages = messageService.getMessagesByTicketId(ticketId);
        return Response.ok(messages).build();
    }

    // DELETE message by id
    @DELETE
    @Path("/{id}")
    public boolean delete(@PathParam("id") UUID id) {
        return messageService.delete(id);
    }
}
