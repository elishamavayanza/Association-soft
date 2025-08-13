package com.org.testApi.repository.custom;

import com.org.testApi.models.MemberRoleHistory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class MemberRoleHistoryRepositoryImpl implements MemberRoleHistoryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MemberRoleHistory> searchMemberRoleHistoryComplexQuery(String role, Long memberId, LocalDate startDate, LocalDate endDate, Boolean isActive) {
        StringBuilder jpql = new StringBuilder("SELECT mrh FROM MemberRoleHistory mrh JOIN FETCH mrh.member WHERE 1=1");
        if (role != null && !role.isEmpty()) {
            jpql.append(" AND LOWER(mrh.role) LIKE LOWER(CONCAT('%', :role, '%'))");
        }
        if (memberId != null) {
            jpql.append(" AND mrh.member.id = :memberId");
        }
        if (startDate != null) {
            jpql.append(" AND mrh.startDate >= :startDate");
        }
        if (endDate != null) {
            jpql.append(" AND (mrh.endDate IS NULL OR mrh.endDate <= :endDate)");
        }
        if (isActive != null) {
            if (isActive) {
                jpql.append(" AND mrh.endDate IS NULL");
            } else {
                jpql.append(" AND mrh.endDate IS NOT NULL");
            }
        }

        jpql.append(" ORDER BY mrh.startDate DESC");

        TypedQuery<MemberRoleHistory> query = entityManager.createQuery(jpql.toString(), MemberRoleHistory.class);
        if (role != null && !role.isEmpty()) {
            query.setParameter("role", role);
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
    public List<MemberRoleHistory> findMemberRoleHistoryWithMembers(Long associationId, int limit) {
        String jpql = """
            SELECT mrh FROM MemberRoleHistory mrh 
            JOIN FETCH mrh.member m 
            JOIN FETCH m.user 
            JOIN FETCH m.association 
            WHERE m.association.id = :associationId 
            ORDER BY mrh.startDate DESC
            """;
        return entityManager.createQuery(jpql, MemberRoleHistory.class)
                .setParameter("associationId", associationId)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Object[]> findMembersWithCurrentRoles(Long associationId) {
        String jpql = """
            SELECT m, mrh.role 
            FROM Member m 
            LEFT JOIN MemberRoleHistory mrh ON mrh.member = m AND mrh.endDate IS NULL 
            WHERE m.association.id = :associationId 
            AND m.leaveDate IS NULL 
            ORDER BY m.joinDate DESC
            """;
        return entityManager.createQuery(jpql, Object[].class)
                .setParameter("associationId", associationId)
                .getResultList();
    }
}
