package org.stll.reply.core.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class ValidationResponse {
    private UUID userId;
    private Set<String> roles;
}
