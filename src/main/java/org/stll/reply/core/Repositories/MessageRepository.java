package org.stll.reply.core.Repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.extern.jbosslog.JBossLog;
import org.stll.reply.core.Entities.Message;

import java.util.*;

@ApplicationScoped
@JBossLog
public class MessageRepository {

    @Inject
    EntityManager em;

    public Message save(Message message) {
        log.info("MessageRepository : Trying to save new message from user id: " + message.getUserId());

        em.createNativeQuery(
                        "INSERT INTO ticket_messages (ticket_id, user_id, message) VALUES (?, ?, ?)"
                )
                .setParameter(1, message.getTicketId())
                .setParameter(2, message.getUserId())
                .setParameter(3, message.getMessage())
                .executeUpdate();

        // Retrieve the ID from the saved message.
        //   Search messages by User UUID
        //   Return id of most recent message found
        // Optional<UUID> savedMessageId = findIdOfLastMessageCreatedByUserId(message.getUserId());
        // savedMessageId.ifPresent(message::setId);
        log.info("MessageRepository: Successfully saved message! Trying to retrieve message id");


        try {
            // Retrieve the saved message
            Optional<Message> savedMessage = findLastMessageCreatedByUserId(message.getUserId());
            log.info("MessageRepository: Retrieving saved message ID" + savedMessage.get().getId());
            return savedMessage.get();
        } catch (NoSuchElementException e){
            log.error("MessageRepository: Failed to retrieve saved message ID");
            throw new InternalServerErrorException("Failed to retrieve the saved message.");
        }
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

    public List<Message> findAllMessagesByTicketId(UUID ticketId) {
        List<Message> messages;

        try {
            messages = em.createNativeQuery(
                            "SELECT id, ticket_id, message, created_at, user_id FROM ticket_messages WHERE ticket_id = ?"
                    )
                    .setParameter(1, ticketId)
                    .getResultList();

        } catch (jakarta.persistence.NoResultException e) {
            messages = Collections.emptyList();
        }

        return messages;
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
