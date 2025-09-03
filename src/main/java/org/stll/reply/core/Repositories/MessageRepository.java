package org.stll.reply.core.Repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;
import org.stll.reply.core.Entities.Message;

import java.time.LocalDateTime;
import java.util.*;

@ApplicationScoped
@JBossLog
public class MessageRepository {

    @Inject
    EntityManager em;

    public Message save(Message message) {
        log.info("MessageRepository : Trying to save new message from user id: " + message.getUserId());

        Query query = em.createNativeQuery(
                        "INSERT INTO ticket_messages (ticket_id, user_id, message) VALUES (?, ?, ?) RETURNING id, message, created_at"
                );
                query.setParameter(1, message.getTicketId());
                query.setParameter(2, message.getUserId());
                query.setParameter(3, message.getMessage());

        Object[] result = (Object[]) query.getSingleResult();

        UUID generatedId = (UUID) result[0];
        String messageContent = (String) result[1];
        LocalDateTime createdAt = (LocalDateTime) result[2];

        message.setId(generatedId);
        message.setMessage(messageContent);
        message.setCreatedAt(createdAt);

        log.info("MessageRepository: Successfully saved message with ID : " + message.getId());

        return message;
    }

    @Transactional
    public Message update(Message message) {
        log.info("TicketRepository: Received message to update with ID : " + message.getId());

        int rowsAffected = em.createNativeQuery(
                        "UPDATE ticket_messages SET message = ? WHERE id = ?"
                )
                .setParameter(1, message.getMessage())
                .setParameter(2, message.getId())
                .executeUpdate();

        return em.find(Message.class, message.getId());
    }

    public Optional<Message> findById(UUID messageId) {
        try {
            Message message = (Message) em.createNativeQuery(
                            "SELECT id, ticket_id, message, created_at, user_id FROM ticket_messages WHERE id = ?", Message.class
                    ).setParameter(1, messageId)
                    .getSingleResult();

            return Optional.of(message);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Message> findAllMessagesByTicketId(UUID ticketId) {
        try {
            return (List<Message>) em.createNativeQuery(
                            "SELECT id, ticket_id, message, created_at, user_id FROM ticket_messages WHERE ticket_id = ?", Message.class
                    )
                    .setParameter(1, ticketId)
                    .getResultList();

        } catch (jakarta.persistence.NoResultException e) {
            return Collections.emptyList();
        }
    }

    public Optional<UUID> findIdOfLastMessageCreatedByUserId(UUID userId) {

        try {
            UUID messageId = (UUID) em.createNativeQuery(
                            "SELECT id FROM ticket_messages WHERE user_id = ? ORDER BY created_at DESC"
                    )
                    .setParameter(1, userId)
                    .setMaxResults(1)
                    .getSingleResult();

            return Optional.of(messageId);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Message> findLastMessageCreatedByUserId(UUID userId) {
        log.info("MessageRepository: Trying to find last message created by user id: " + userId);
        try {
            return Optional.of(
                    (Message) em.createNativeQuery(
                                    "SELECT * FROM ticket_messages WHERE user_id = ? ORDER BY created_at DESC", Message.class
                            )
                            .setParameter(1, userId)
                            .setMaxResults(1)
                            .getSingleResult()
            );

        } catch (NoResultException e) {
            log.warn("MessageRepository: No messages found for user ID: " + userId);
            return Optional.empty();
        }
    }

    @Transactional
    public boolean deleteById(UUID id) {
        int rowsAffected = em.createNativeQuery("DELETE FROM ticket_messages WHERE id = ?")
                .setParameter(1, id)
                .executeUpdate();

        return rowsAffected > 0;
    }
}
