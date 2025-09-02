package org.stll.reply.core.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@RegisterForReflection
@Schema(
        example = "{\"email\": \"aaa@email.com\"}"
)
public class UpdateUserEmailRequest {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    public String email;
}
