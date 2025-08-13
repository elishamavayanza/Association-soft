package com.org.testApi.repository.custom;

import com.org.testApi.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> searchUsersComplexQuery(String username, String email, String firstName, String lastName, Integer roleId) {
        StringBuilder jpql = new StringBuilder("SELECT DISTINCT u FROM User u");
        if (roleId != null) {
            jpql.append(" JOIN u.roles r");
        }
        jpql.append(" WHERE 1=1");

        if (username != null && !username.isEmpty()) {
            jpql.append(" AND LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))");
        }
        if (email != null && !email.isEmpty()) {
            jpql.append(" AND LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))");
        }
        if (firstName != null && !firstName.isEmpty()) {
            jpql.append(" AND LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))");
        }
        if (lastName != null && !lastName.isEmpty()) {
            jpql.append(" AND LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))");
        }
        if (roleId != null) {
            jpql.append(" AND r.id = :roleId");
        }

        jpql.append(" ORDER BY u.username ASC");

        TypedQuery<User> query = entityManager.createQuery(jpql.toString(), User.class);
        if (username != null && !username.isEmpty()) {
            query.setParameter("username", username);
        }
        if (email != null && !email.isEmpty()) {
            query.setParameter("email", email);
        }
        if (firstName != null && !firstName.isEmpty()) {
            query.setParameter("firstName", firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            query.setParameter("lastName", lastName);
        }
        if (roleId != null) {
            query.setParameter("roleId", roleId);
        }

        return query.getResultList();
    }

    @Override
    public List<User> findUsersWithRoles(int limit) {
        String jpql = """
            SELECT DISTINCT u FROM User u 
            LEFT JOIN FETCH u.roles 
            ORDER BY u.username ASC
            """;
        return entityManager.createQuery(jpql, User.class)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Object[]> findInactiveUsersWithEventCount(LocalDateTime sinceDate) {
        String jpql = """
            SELECT u, COUNT(a) 
            FROM User u 
            LEFT JOIN u.attendedEvents a 
            WHERE u.lastLogin < :sinceDate OR u.lastLogin IS NULL 
            GROUP BY u.id 
            ORDER BY u.lastLogin ASC
            """;
        return entityManager.createQuery(jpql, Object[].class)
                .setParameter("sinceDate", sinceDate)
                .getResultList();
    }
}
