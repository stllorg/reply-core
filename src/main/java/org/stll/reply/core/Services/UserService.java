package org.stll.reply.core.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;
import org.stll.reply.core.Entities.Role;
import org.stll.reply.core.Entities.User;
import org.stll.reply.core.Repositories.UserRepository;
import org.stll.reply.core.dtos.PaginationResponse;
import org.stll.reply.core.utils.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@JBossLog
public class UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    public User createUserFromRequest(String username, String email, String password) {

        log.info("UserService - Received email {}" + email);
        if (findUserByEmail(email).isPresent()) {
            log.info("UserService - User email already exists! {}" + email);
            throw new EntityExistsException();
        }
        String hashedPassword = passwordEncoder.hashPassword(password);

        User user = new User(username, email, hashedPassword);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashedPassword);

        log.info("UserService - Received email from user {}" + user.getEmail());
        createUser(user);

        log.info("UserService - Sucesscfully created user with email: " + user.getEmail());
        return user;
    }

    @Transactional
    public User createUser(User user) {
        // Validate if username or email already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        // Save the user
        User savedUser = userRepository.save(user);

        // Assign the default role (ID 4) to the new user
        userRepository.assignRoleToUser(savedUser.getId(), 4);

        return savedUser;
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findUserByUsername(String username) {
        log.info("UserService Trying to find username: " + username);
        return userRepository.findByUsername(username);
    }

    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    public PaginationResponse<User> getUsers(int page, int limit) {
        return userRepository.findAll(page, limit);
    }

    public boolean delete(UUID id) {
        return userRepository.deleteById(id);
    }

    public String[] getUserRolesByUserId(UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userRepository.findRolesByUserId(userId).stream()
                    .map(Role::getName)
                    .toArray(String[]::new);
        }
        return new String[]{};
    }

    @Transactional
    public boolean updateUserRoles(UUID userId, List<Integer> rolesIdsList) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            userRepository.updateUserRoles(userId, rolesIdsList);
        }
        return false;
    }

    @Transactional
    public boolean insertUserRole(UUID userId, int roleId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            userRepository.assignRoleToUser(userId, roleId);
        }
        return false;
    }

    @Transactional
    public boolean deleteAllUserRoles(UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            userRepository.deleteRolesByUserId(userId);
            return true;
        }
        return false;
    }
}
