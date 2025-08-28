package org.stll.reply.core.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name= "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String status;

    @Column(name = "is_repeat", nullable = false)
    private boolean isRepeat = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    public Ticket(String subject, UUID userId) {
        this.subject = subject;
        this.userId = userId;
    }
}
