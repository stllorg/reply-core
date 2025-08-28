package org.stll.reply.core.utils;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PasswordEncoder {

    public String hashPassword(String password) {
        return BcryptUtil.bcryptHash(password);
    }

    public boolean checkPassword(String password, String hashedPassword) {
        return BcryptUtil.matches(password, hashedPassword);
    }
}
