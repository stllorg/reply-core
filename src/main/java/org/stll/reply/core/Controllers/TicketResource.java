package org.stll.reply.core.Controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.stll.reply.core.Entities.Ticket;
import org.stll.reply.core.Services.TicketService;
import org.stll.reply.core.dtos.CreateTicketRequest;
import org.stll.reply.core.dtos.SaveTicketResponse;
import org.stll.reply.core.dtos.UpdateTicketRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/tickets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@JBossLog
public class TicketResource {

    @Inject
    TicketService ticketService;

    @Inject
    JsonWebToken jwt;

    // CREATE ticket
    @POST
    @RolesAllowed("user")
    public Response createTicket(CreateTicketRequest request) {
        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Ticket data is missing.").build();
        }

        log.info("TicketResource Received token for validation");
        String userIdString = jwt.getClaim("id").toString();

        UUID userId = null;

        try {
            userId = UUID.fromString(userIdString);
        } catch (IllegalArgumentException e) {
            log.error("Failed to convert the user id from JWT claim to UUID:");
            userId = null;
        }

        if (userId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An auth token with user id is required").build();
        }

        if (request.subject == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Subject is required").build();
        }

        try {
            Ticket createdTicket = ticketService.createTicket(request.subject, userId);

            SaveTicketResponse saveTicketResponse = new SaveTicketResponse(createdTicket.getSubject(), createdTicket.getId(), createdTicket.getUserId());
            // return Response.created(URI.create("/tickets/" + createdTicket.getId())).entity(createdTicket).build();
            return Response.ok(saveTicketResponse).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // GET ticket
    @GET
    @Path("/{id}")
    public Response getTicketById(@PathParam("id") UUID id) {
        return ticketService.findTicketById(id)
                .map(ticket -> Response.ok(ticket).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    // UPDATE ticket
    @PUT
    @Path("/{id}")
    public Response updateTicket(@PathParam("id") UUID id, UpdateTicketRequest request) {
        Optional <Ticket> ticketOptional = ticketService.findTicketById(id);

        if (ticketOptional.isPresent()) {
            Ticket ticketToUpdate = ticketOptional.get();
            ticketToUpdate.setSubject(request.subject);

            return ticketService.updateTicket(ticketToUpdate)
                    .map(ticket -> Response.ok(ticket).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // UPDATE ticket status to closed to archive ticket
    @PUT
    @Path("/{id}/archive")
    @RolesAllowed({"admin", "manager", "user"})
    public Response archiveTicket(@PathParam("id") UUID id) {
        Optional<Ticket> ticketOptional = ticketService.findTicketById(id);

        if (ticketOptional.isPresent()) {
            Ticket ticketToArchive = ticketOptional.get();
            ticketToArchive.setStatus("closed");

            return ticketService.updateTicket(ticketToArchive)
                    .map(ticket -> Response.ok(ticket).build())
                    .orElse(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
        } else {
            // Ticket was not found, return 404.
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // DELETE ticket
    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response deleteTicketById(@PathParam("id") UUID id) {
        boolean isDeleted = ticketService.delete(id);
        if (isDeleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // GET ALL Open TICKETS
    @GET
    @Path("/open")
    public Response getAllOpenTickets() {
        List<Ticket> openTickets = ticketService.findAllOpenTickets();
        return Response.ok(openTickets).build();
    }

    // GET ALL TICKETS created by a User
    @GET
    @Path("/user/{userId}")
    public Response getTicketsByUserId(@PathParam("userId") UUID id) {
        List<Ticket> tickets = ticketService.findTicketsByUserId(id);
        return Response.ok(tickets).build();
    }

    // GET Tickets Ids with User Messages By User Id
    @GET
    @Path("/user/{userId}/tickets")
    public Response getTicketIdsWithUserMessagesByUserId(@PathParam("userId") UUID userId) {
        List<UUID> ticketIds = ticketService.getTicketIdsWithUserMessagesByUserId(userId);
        return Response.ok(ticketIds).build();
    }
}
