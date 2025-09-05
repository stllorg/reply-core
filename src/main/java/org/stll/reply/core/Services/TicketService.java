package org.stll.reply.core.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.stll.reply.core.Entities.Ticket;
import org.stll.reply.core.Repositories.TicketRepository;
import org.stll.reply.core.dtos.PaginationResponse;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TicketService {

    @Inject
    TicketRepository ticketRepository;

    // CREATE ticket
    @Transactional
    public Ticket createTicket(String subject, UUID userId) {

        Ticket ticket = new Ticket(subject, userId);
        return ticketRepository.save(ticket);
    }

    // FIND all tickets created by USER id
    @Transactional
    public PaginationResponse<Ticket> findTicketsByUserId(UUID userId, int page, int limit) {
        return ticketRepository.findAllTicketsByUserId(userId, page, limit);
    }

    // FIND all open tickets
    @Transactional
    public PaginationResponse<Ticket> findAllOpenTickets(int page, int limit) {
        return ticketRepository.findAllOpenTickets(page, limit);
    }

    // FIND one ticket by Ticket id
    public Optional<Ticket> findTicketById(UUID ticketId) {
        return ticketRepository.findById(ticketId);
    }

    // FIND one ticket creator by ticket id
    public Optional<UUID> findTicketCreatorId(UUID ticketId) {
        return ticketRepository.findUserIdByTicketId(ticketId);
    }

    // FIND ALL tickets Ids with messages by user
    public PaginationResponse<UUID> getTicketIdsWithUserMessagesByUserId(UUID userId, int page, int limit) {
        return ticketRepository.findAllTicketIdWithUserMessages(userId, page, limit);
    }

    // UPDATE ticket
    public Optional<Ticket> updateTicket(Ticket ticket) {
        return Optional.of(ticketRepository.update(ticket));
    }

    // DELETE ticket
    public boolean delete(UUID ticketId) {
        return ticketRepository.deleteById(ticketId);
    }
}
