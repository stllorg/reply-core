package org.stll.reply.core.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

// TODO: The current type for roles is String to temporary allow role id or role name as role
@Data
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class RoleUpdateRequest {

    @Schema(
            example = "[\"manager\", \"support\", \"user\"]",
            description = "A list of role names to be assigned to the user."
    )
    private List<String> roleNames;
}