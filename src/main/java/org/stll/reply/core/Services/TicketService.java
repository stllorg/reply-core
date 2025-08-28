package org.stll.reply.core.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.stll.reply.core.Entities.Ticket;
import org.stll.reply.core.Repositories.TicketRepository;

import java.util.List;
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
    public List<Ticket> findTicketsByUserId(UUID userId) {
        return ticketRepository.findAllTicketsByUserId(userId);
    }

    // FIND all open tickets
    @Transactional
    public List<Ticket> findAllOpenTickets() {
        return ticketRepository.findAllOpenTickets();
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
    public List<UUID> getTicketIdsWithUserMessagesByUserId(UUID userId) {
        return ticketRepository.findAllTicketIdWithUserMessages(userId);
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
