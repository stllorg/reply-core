package org.stll.reply.core.Repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;
import org.stll.reply.core.Entities.Ticket;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
@JBossLog
public class TicketRepository {

    @Inject
    EntityManager em;

    public Ticket save(Ticket ticket) {
        log.info("TicketRepository : Trying to save new ticket from user id: " + ticket.getUserId());

        em.createNativeQuery(
                        "INSERT INTO tickets (subject, user_id) VALUES (?, ?)"
                )
                .setParameter(1, ticket.getSubject())
                .setParameter(2, ticket.getUserId())
                .executeUpdate();

        // After insertion, retrieve the ID from the saved Ticket.
        Optional<UUID> savedTicketId = findIdOfLastTicketCreatedByUserId(ticket.getUserId());
        savedTicketId.ifPresent(ticket::setId);
        log.info("TicketRepository: Successfully saved ticket with ID : " + ticket.getId());

        return ticket;
    }

    public List<Ticket> findAllTicketsByUserId(UUID userId) {

        List<Ticket> tickets;

        try {
            tickets = em.createNativeQuery(
                        "SELECT id, subject, status, created_at FROM tickets WHERE user_id = ?"
                )
                .setParameter(1, userId)
                .getResultList();

        } catch (jakarta.persistence.NoResultException e) {
            tickets = Collections.emptyList();
        }

        return tickets;
    }

    public List<Ticket> findAllOpenTickets() {

        List<Ticket> tickets;

        try {
            tickets = em.createNativeQuery(
                            "SELECT t.id, t.subject, t.created_at FROM tickets t JOIN users u ON t.user_id = u.id WHERE t.status = 'open' "
                    )
                    .getResultList();

        } catch (jakarta.persistence.NoResultException e) {
            tickets = Collections.emptyList();
        }

        return tickets;
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

    public List<UUID> findAllTicketIdWithUserMessages(UUID userId) {

        List<UUID> ticketsIds;
        try {
            List<Object> foundTicketsIds = em.createNativeQuery(
                            "SELECT DISTINCT ticket_id FROM ticket_messages WHERE user_id = ?"
                    )
                    .setParameter(1, userId)
                    .getResultList();

            ticketsIds = (List<UUID>) foundTicketsIds.stream()
                    .map(obj -> (UUID) obj)
                    .collect(Collectors.toList());

            log.info("TicketRepository: Tickets Found  : " + ticketsIds.size());
        } catch (jakarta.persistence.NoResultException e) {
            ticketsIds = Collections.emptyList();
        }

        return ticketsIds;
    }

    @Transactional
    public boolean deleteById(UUID id) {
        int rowsAffected = em.createNativeQuery("DELETE FROM tickets WHERE id = ?")
                .setParameter(1, id)
                .executeUpdate();

        return rowsAffected > 0;
    }
}
