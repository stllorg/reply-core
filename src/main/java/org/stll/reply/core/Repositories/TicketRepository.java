package org.stll.reply.core.Repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;
import org.stll.reply.core.Entities.Ticket;
import org.stll.reply.core.dtos.PaginationResponse;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
@JBossLog
public class TicketRepository {

    @Inject
    EntityManager em;

    public Ticket save(Ticket ticket) {
        log.info("TicketRepository : Trying to save new ticket from user id: " + ticket.getUserId());

        Query query = em.createNativeQuery(
                "INSERT INTO tickets (subject, user_id) VALUES (?, ?) RETURNING id, status, is_repeat, created_at"
        );
        query.setParameter(1, ticket.getSubject());
        query.setParameter(2, ticket.getUserId());

        Object[] result = (Object[]) query.getSingleResult();

        UUID generatedId = (UUID) result[0];
        String status = (String) result[1];
        Boolean isRepeat = (Boolean) result[2];
        LocalDateTime createdAt = (LocalDateTime) result[3];

        ticket.setId(generatedId);
        ticket.setStatus(status);
        ticket.setRepeat(isRepeat);
        ticket.setCreatedAt(createdAt);


        log.info("TicketRepository: Successfully saved ticket with ID : " + ticket.getId());

        return ticket;
    }

    // PAGINATION - FIND ALL TICKETS
    @SuppressWarnings("unchecked")
    public PaginationResponse<Ticket> findAllTicketsByUserId(UUID userId, int page, int limit) {
        int offset = (page - 1) * limit;

        // 1. Query to count total number of users
        long totalTickets = (long) em.createNativeQuery("SELECT COUNT(*) FROM tickets WHERE user_id = ?"
        )
        .setParameter(1, userId)
        .getSingleResult();

            // 2. Query to find messages for the given page based on LIMIT and OFFSET
            List<Ticket> tickets = (List<Ticket>) em.createNativeQuery(
                        "SELECT id, subject, status, created_at FROM tickets WHERE user_id = ? ORDER BY created_at ASC LIMIT ? OFFSET ?", Ticket.class
                )
                .setParameter(1, userId)
                .setParameter(2, limit)
                .setParameter(3, offset)
                .getResultList();
        
        long totalPages = (long) Math.ceil((double) totalTickets / limit);

        // 3. Build the pagination object
        return new PaginationResponse<>(tickets, page, limit, totalTickets, totalPages);
        
    }

    // PAGINATION - FIND ALL OPEN TICKETS
    @SuppressWarnings("unchecked")
    public PaginationResponse<Ticket> findAllOpenTickets(int page, int limit) {
        int offset = (page - 1) * limit;

        
        long totalTickets = (long) em.createNativeQuery(
            "SELECT COUNT(*) FROM tickets t WHERE t.status = 'open'"
        )
        .getSingleResult();


            List<Ticket> tickets = (List<Ticket>) em.createNativeQuery(
                            "SELECT t.id, t.subject, t.created_at FROM tickets t JOIN users u ON t.user_id = u.id WHERE t.status = 'open' ORDER BY t.created_at ASC LIMIT ? OFFSET ?", Ticket.class
                    )
                    .setParameter(1, limit)
                    .setParameter(2, offset)
                    .getResultList();
        
        long totalPages = (long) Math.ceil((double) totalTickets / limit);

        return new PaginationResponse<>(tickets, page, limit, totalTickets, totalPages);
    }

    // Should find the id of the most recent ticket created by user
    public Optional<UUID> findIdOfLastTicketCreatedByUserId(UUID userId) {
        try {
            UUID ticketId = (UUID) em.createNativeQuery(
                            "SELECT id FROM tickets WHERE user_id = ? ORDER BY created_at DESC"
                    )
                    .setParameter(1, userId)
                    .setMaxResults(1)
                    .getSingleResult();

            return Optional.of(ticketId);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Ticket> findById(UUID ticketId) {
        try {
            Ticket ticket = (Ticket) em.createNativeQuery(
                    "SELECT id, subject, status, is_repeat, created_at, user_id FROM tickets WHERE id = ?", Ticket.class
            ).setParameter(1, ticketId)
                    .getSingleResult();

            return Optional.of(ticket);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<UUID> findUserIdByTicketId(UUID ticketId) {
        try {
            Ticket ticket = (Ticket) em.createNativeQuery(
                            "SELECT id, user_id FROM tickets WHERE id = ?", Ticket.class
                    ).setParameter(1, ticketId)
                    .getSingleResult();

            // Return user id
            return Optional.of(ticket.getUserId());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public Ticket update(Ticket ticket) {
        log.info("TicketRepository: Received ticket to update with ID : " + ticket.getId());

        int rowsAffected = em.createNativeQuery(
                        "UPDATE tickets SET subject = ?, status = ?::ticket_status WHERE id = ?"
                )
                .setParameter(1, ticket.getSubject())
                .setParameter(2, ticket.getStatus())
                .setParameter(3, ticket.getId())
                .executeUpdate();

        return em.find(Ticket.class, ticket.getId());
    }

    // PAGINATION - FIND ALL TICKETS
    @SuppressWarnings("unchecked")
    public PaginationResponse<UUID> findAllTicketIdWithUserMessages(UUID userId, int page, int limit) {
        int offset = (page -1) * limit;

        // 1. Query to count total number of users
        long totalTickets = (long) em.createNativeQuery(
            "SELECT COUNT (DISTINCT ticket_id) FROM ticket_messages WHERE user_id = ?"
        )
        .setParameter(1, userId)
        .getSingleResult();

        // 2. Query to find messages for the given page based on LIMIT and OFFSET
            List<Object> foundTicketsIds = em.createNativeQuery(
                            "SELECT DISTINCT ticket_id FROM ticket_messages WHERE user_id = ? ORDER BY ticket_id ASC LIMIT ? OFFSET ?"
                    )
                    .setParameter(1, userId)
                    .setParameter(2, limit)
                    .setParameter(3, offset)
                    .getResultList();

            List<UUID> ticketsIds = (List<UUID>) foundTicketsIds.stream()
                    .map(obj -> (UUID) obj)
                    .collect(Collectors.toList());

            log.info("TicketRepository: Tickets Found  : " + ticketsIds.size());

        // 3. Build the pagination object
        long totalPages = (long) Math.ceil((double) totalTickets / limit);


        return new PaginationResponse<>(ticketsIds, page, limit, totalTickets, totalPages);
    }

    @Transactional
    public boolean deleteById(UUID id) {
        int rowsAffected = em.createNativeQuery("DELETE FROM tickets WHERE id = ?")
                .setParameter(1, id)
                .executeUpdate();

        return rowsAffected > 0;
    }
}
