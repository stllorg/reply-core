package org.stll.reply.core.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class UserDTO {
    private UUID id;
    private String username;
    private String email;
}