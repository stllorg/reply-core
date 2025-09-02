package org.stll.reply.core.Repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;
import org.stll.reply.core.Entities.Role;
import org.stll.reply.core.Entities.User;
import org.stll.reply.core.dtos.PaginationResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@JBossLog
public class UserRepository {

    @Inject
    EntityManager em;

    @Transactional
    public User save(User user) {
        em.createNativeQuery(
                        "INSERT INTO users (username, email, password) VALUES (?, ?, ?)"
                )
                .setParameter(1, user.getUsername())
                .setParameter(2, user.getEmail())
                .setParameter(3, user.getPassword())
                .executeUpdate();

        // After insertion, retrieve the generated ID.
        User savedUser = findByEmail(user.getEmail())
                .orElseThrow(() -> new IllegalStateException("User not found after saving"));
        user.setId(savedUser.getId());
        return user;
    }

    @Transactional
    public Optional<User> update(User user) {
        int rowsAffected = em.createNativeQuery(
                            "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?"
                    )
                    .setParameter(1, user.getUsername())
                    .setParameter(2, user.getEmail())
                    .setParameter(3, user.getPassword())
                    .setParameter(4, user.getId())
                    .executeUpdate();

        User updatedUser = em.find(User.class, user.getId());
        if (rowsAffected > 0 ) {
            return Optional.of(updatedUser);
        }

        return Optional.empty();
    }

    @Transactional
    public boolean deleteById(UUID id) {

        // Remove user from user_roles table
        em.createNativeQuery("DELETE FROM user_roles WHERE user_id = ?")
                .setParameter(1, id)
                .executeUpdate();

        int rowsAffected =  em.createNativeQuery("DELETE FROM users WHERE id = ?")
                .setParameter(1, id)
                .executeUpdate();

        return rowsAffected > 0;
    }

    public Optional<User> findByEmail(String email) {
        try {
            User user = (User) em.createNativeQuery(
                            "SELECT id, username, email, password, created_at FROM users WHERE email = ?", User.class
                    )
                    .setParameter(1, email)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByUsername(String username) {
        try {
            User user = (User) em.createNativeQuery(
                            "SELECT id, username, email, password, created_at FROM users WHERE username = ?", User.class
                    )
                    .setParameter(1, username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findById(UUID id) {
        try {
            User user = (User) em.createNativeQuery(
                            "SELECT id, username, email, password, created_at FROM users WHERE id = ?", User.class
                    )
                    .setParameter(1, id)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    public PaginationResponse<User> findAll(int page, int limit) {
        int offset = (page - 1) * limit;

        // 1. Query to count total number of users
        long totalUsers = (long) em.createNativeQuery("SELECT COUNT(*) FROM users")
                .getSingleResult();

        // 2. Query to find users of given page based on LIMIT and OFFSET
        List<User> users  = (List<User>) em.createNativeQuery(
                            "SELECT id, username, email FROM users ORDER BY id ASC LIMIT ? OFFSET ?", User.class
                    )
                    .setParameter(1, limit)
                    .setParameter(2, offset)
                    .getResultList();


        // 3. Build the Pagination Object
        long totalPages = (long) Math.ceil((double) totalUsers / limit);

        return new PaginationResponse<>(users, page, limit, totalUsers, totalPages);
    }

    @SuppressWarnings("unchecked")
    public List<Role> findRolesByUserId(UUID userId) {
        return (List<Role>) em.createNativeQuery(
                        "SELECT r.id, r.name FROM roles r JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = ?", Role.class
                )
                .setParameter(1, userId)
                .getResultList();
    }

    @Transactional
    public void assignRoleToUser(UUID userId, int roleId) {
        em.createNativeQuery(
                        "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)"
                )
                .setParameter(1, userId)
                .setParameter(2, roleId)
                .executeUpdate();
    }

    @Transactional
    public void updateUserRoles(UUID userId, List<Integer> rolesIds) {

        for (Integer roleId : rolesIds) {
            em.createNativeQuery(
                            "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)"
                    )
                    .setParameter(1, userId)
                    .setParameter(2, roleId)
                    .executeUpdate();
        }

    }

    @Transactional
    public boolean deleteRolesByUserId(UUID userId) {
        int rowsAffected = em.createNativeQuery(
                        "DELETE FROM user_roles WHERE user_id = ?"
                )
                .setParameter(1, userId)
                .executeUpdate();

        return rowsAffected > 0;
    }
}