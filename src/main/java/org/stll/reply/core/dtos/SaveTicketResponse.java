package org.stll.reply.core.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class SaveTicketResponse {
    public String subject;
    public UUID id;
    public UUID userId;
}
