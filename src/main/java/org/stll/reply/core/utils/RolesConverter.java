package org.stll.reply.core.utils;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RolesConverter {
    
    // map lista of RoleNames To list of Ids
    public List<Integer> execute(List<String> roleNames) {
        return roleNames.stream().map(role -> {
            switch (role.toLowerCase()) {
                case "admin": return 1;
                case "manager": return 2;
                case "support": return 3;
                case "user":
                default: return 4;
            }
        }).collect(Collectors.toList());
    }
}
