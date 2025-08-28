package org.stll.reply.core.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// TODO: The current type for roles is String to temporary allow role id or role name as role
@Data
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class RoleUpdateRequest {
    private List<String> roleNames;
}