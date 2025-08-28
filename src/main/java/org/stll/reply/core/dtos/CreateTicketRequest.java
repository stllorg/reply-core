package org.stll.reply.core.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@RegisterForReflection
@Schema(
        example = "{\"subject\": \"Assunto do novo ticket\"}"
)
public class CreateTicketRequest {
    public String subject;
}
