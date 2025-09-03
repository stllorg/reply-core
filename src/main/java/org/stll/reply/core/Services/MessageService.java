package org.stll.reply.core.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.stll.reply.core.Entities.Message;
import org.stll.reply.core.Repositories.MessageRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class MessageService {

    @Inject
    MessageRepository messageRepository;

    // CREATE message
    @Transactional
    public Message createMessage(UUID ticketId, UUID userId, String messageText) {
        Message message = new Message(ticketId, userId, messageText);

        return messageRepository.save(message);
    }

    // FIND one message by Ticket id
    public Optional<Message> findMessageById(UUID messageId) {
        return messageRepository.findById(messageId);
    }

    // FIND ALL messages by ticket id
    public List<Message> getMessagesByTicketId(UUID ticketId) {
        return messageRepository.findAllMessagesByTicketId(ticketId);
    }

    // UPDATE message
    public Optional<Message> updateMessage(Message message) {
        return Optional.of(messageRepository.update(message));
    }

    // DELETE message by id
    public boolean delete(UUID id) {
        return messageRepository.deleteById(id);
    }
}
