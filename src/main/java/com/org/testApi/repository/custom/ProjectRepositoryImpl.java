package com.org.testApi.repository.custom;

import com.org.testApi.models.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Project> searchProjectsComplexQuery(
            String name,
            Project.ProjectStatus status,
            Long associationId,
            Long managerId,
            LocalDate startDate,
            LocalDate endDate) {
        StringBuilder jpql = new StringBuilder("SELECT p FROM Project p WHERE 1=1");
        if (name != null && !name.isEmpty()) {
            jpql.append(" AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))");
        }
        if (status != null) {
            jpql.append(" AND p.status = :status");
        }
        if (associationId != null) {
            jpql.append(" AND p.association.id = :associationId");
        }
        if (managerId != null) {
            jpql.append(" AND p.manager.id = :managerId");
        }
        if (startDate != null) {
            jpql.append(" AND p.startDate >= :startDate");
        }
        if (endDate != null) {
            jpql.append(" AND (p.endDate IS NULL OR p.endDate <= :endDate)");
        }

        jpql.append(" ORDER BY p.startDate DESC");

        TypedQuery<Project> query = entityManager.createQuery(jpql.toString(), Project.class);
        if (name != null && !name.isEmpty()) {
            query.setParameter("name", name);
        }
        if (status != null) {
            query.setParameter("status", status);
        }
        if (associationId != null) {
            query.setParameter("associationId", associationId);
        }
        if (managerId != null) {
            query.setParameter("managerId", managerId);
        }
        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }

        return query.getResultList();
    }

    @Override
    public List<Project> findProjectsWithAssociations(Long associationId, int limit) {
        String jpql = """
            SELECT p FROM Project p 
            JOIN FETCH p.association 
            LEFT JOIN FETCH p.manager 
            WHERE p.association.id = :associationId 
            ORDER BY p.startDate DESC
            """;
        return entityManager.createQuery(jpql, Project.class)
                .setParameter("associationId", associationId)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Object[]> findProjectsWithCounts(Long associationId) {
        String jpql = """
            SELECT p, 
                   (SELECT COUNT(pm) FROM ProjectMember pm WHERE pm.project = p AND pm.leaveDate IS NULL),
                   (SELECT COUNT(a) FROM Activity a WHERE a.project = p)
            FROM Project p 
            WHERE p.association.id = :associationId 
            ORDER BY p.startDate DESC
            """;
        return entityManager.createQuery(jpql, Object[].class)
                .setParameter("associationId", associationId)
                .getResultList();
    }
}
