package com.org.testApi.repository.custom;

import com.org.testApi.models.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> searchRolesComplexQuery(String name, String description) {
        StringBuilder jpql = new StringBuilder("SELECT r FROM Role r WHERE 1=1");
        if (name != null && !name.isEmpty()) {
            jpql.append(" AND LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))");
        }
        if (description != null && !description.isEmpty()) {
            jpql.append(" AND LOWER(r.description) LIKE LOWER(CONCAT('%', :description, '%'))");
        }

        jpql.append(" ORDER BY r.name ASC");

        TypedQuery<Role> query = entityManager.createQuery(jpql.toString(), Role.class);
        if (name != null && !name.isEmpty()) {
            query.setParameter("name", name);
        }
        if (description != null && !description.isEmpty()) {
            query.setParameter("description", description);
        }

        return query.getResultList();
    }

    @Override
    public List<Object[]> findRolesWithUserCount(int limit) {
        String jpql = """
            SELECT r, COUNT(u) 
            FROM Role r 
            LEFT JOIN User u ON r MEMBER OF u.roles 
            GROUP BY r.id, r.name, r.description 
            ORDER BY COUNT(u) DESC
            """;
        return entityManager.createQuery(jpql, Object[].class)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Role> findUnusedRoles() {
        String jpql = """
            SELECT r FROM Role r 
            WHERE r.id NOT IN (SELECT DISTINCT r2.id FROM User u JOIN u.roles r2)
            """;
        return entityManager.createQuery(jpql, Role.class)
                .getResultList();
    }
}
