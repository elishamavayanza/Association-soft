package com.org.testApi.repository.custom;

import com.org.testApi.models.ProjectMember;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ProjectMemberRepositoryImpl implements ProjectMemberRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProjectMember> searchProjectMembersComplexQuery(
            String roleInProject,
            Long projectId,
            Long memberId,
            Boolean isActive,
            LocalDate startDate,
            LocalDate endDate) {
        StringBuilder jpql = new StringBuilder("SELECT pm FROM ProjectMember pm JOIN FETCH pm.project JOIN FETCH pm.member WHERE 1=1");
        if (roleInProject != null && !roleInProject.isEmpty()) {
            jpql.append(" AND LOWER(pm.roleInProject) LIKE LOWER(CONCAT('%', :roleInProject, '%'))");
        }
        if (projectId != null) {
            jpql.append(" AND pm.project.id = :projectId");
        }
        if (memberId != null) {
            jpql.append(" AND pm.member.id = :memberId");
        }
        if (isActive != null) {
            if (isActive) {
                jpql.append(" AND pm.leaveDate IS NULL");
            } else {
                jpql.append(" AND pm.leaveDate IS NOT NULL");
            }
        }
        if (startDate != null) {
            jpql.append(" AND pm.joinDate >= :startDate");
        }
        if (endDate != null) {
            jpql.append(" AND (pm.leaveDate IS NULL OR pm.leaveDate <= :endDate)");
        }

        jpql.append(" ORDER BY pm.joinDate DESC");

        TypedQuery<ProjectMember> query = entityManager.createQuery(jpql.toString(), ProjectMember.class);
        if (roleInProject != null && !roleInProject.isEmpty()) {
            query.setParameter("roleInProject", roleInProject);
        }
        if (projectId != null) {
            query.setParameter("projectId", projectId);
        }
        if (memberId != null) {
            query.setParameter("memberId", memberId);
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
    public List<ProjectMember> findProjectMembersWithAssociations(Long associationId, int limit) {
        String jpql = """
            SELECT pm FROM ProjectMember pm 
            JOIN FETCH pm.project p 
            JOIN FETCH p.association 
            JOIN FETCH pm.member m 
            JOIN FETCH m.user 
            WHERE p.association.id = :associationId 
            ORDER BY pm.joinDate DESC
            """;
        return entityManager.createQuery(jpql, ProjectMember.class)
                .setParameter("associationId", associationId)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Object[]> findProjectsWithMemberCount(Long associationId) {
        String jpql = """
            SELECT p, COUNT(pm) 
            FROM Project p 
            LEFT JOIN ProjectMember pm ON pm.project = p AND pm.leaveDate IS NULL 
            WHERE p.association.id = :associationId 
            GROUP BY p.id 
            ORDER BY p.name
            """;
        return entityManager.createQuery(jpql, Object[].class)
                .setParameter("associationId", associationId)
                .getResultList();
    }
}
