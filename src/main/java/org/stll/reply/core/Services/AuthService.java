package org.stll.reply.core.Services;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import lombok.extern.jbosslog.JBossLog;
import org.stll.reply.core.Entities.User;
import org.stll.reply.core.dtos.LoginRequest;
import org.stll.reply.core.utils.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@JBossLog
public class AuthService {

    @Inject
    private UserService userService;

    @Inject
    private PasswordEncoder passwordEncoder;



    public User register(String username, String password, String email) {
        if (userService.findUserByEmail(email).isPresent()) {
            throw new EntityExistsException();
        }
        String hashedPassword = passwordEncoder.hashPassword(password);

        User user = new User(username, email, hashedPassword);
        userService.createUser(user);

        return user;
    }

    public Optional<String> loginWithEmail(LoginRequest request) {
        // request.username is as parameter to represent user.email
        Optional<User> userOptional = userService.findUserByEmail(request.getUsername());

        if (userOptional.isEmpty() || !passwordEncoder.checkPassword(request.getPassword(), userOptional.get().getPassword())) {
            return Optional.empty();
        }

        User user = userOptional.get();
        return generateToken(user);
    }

    public Optional<String> loginWithUsername(LoginRequest request) {
        log.info("AuthService Trying to login username: " + request.getUsername() );
        Optional<User> userOptional = userService.findUserByUsername(request.getUsername());

        if (userOptional.isEmpty()) {
            log.info("AuthService User not found!");
            return Optional.empty();
        }

        if (!passwordEncoder.checkPassword(request.getPassword(), userOptional.get().getPassword())) {
            log.info("AuthService User password is wrong! ");
            return Optional.empty();
        }

        User user = userOptional.get();
        log.info("AuthService User found" + user.getUsername());
        return generateToken(user);
    }

    private Optional<String> generateToken(User user) {
        log.info("AuthService : Trying to generate JWT token" );

        try {


            log.info("AuthService: The private key was found");

            log.info("AuthService: Trying to get roles information");


            String[] roles = userService.getUserRolesByUserId(user.getId());
            Set<String> rolesSet = Arrays.stream(roles).collect(Collectors.toSet());

            log.info("AuthService: Trying to generate a new signed JWT Token");
            String currentUsername = user.getUsername();
            String userIdString = "User id " + user.getId();
            log.info("AuthService: Starting to generate token for user" + currentUsername);
            log.info("AuthService: Trying to generate token for user with Id " + userIdString);
            String token = Jwt.issuer("reply-core")
                    .upn(currentUsername)
                    .groups(rolesSet)
                    .claim("id", user.getId())
                    .expiresAt(System.currentTimeMillis() + 3600)
                    .sign();

            log.info("AuthService The JWT token was generated for user: " + user.getUsername());
            return Optional.of(token);
        } catch (Exception e) {
            log.fatal("Failed while trying to generate JWT token: " + e.getMessage());
            return Optional.empty();
        }
    }
}