package com.org.testApi.repository.custom;

import com.org.testApi.models.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Member> searchMembersComplexQuery(String name, String email, Member.MemberType memberType, Long associationId, Boolean isActive) {
        StringBuilder jpql = new StringBuilder("SELECT m FROM Member m WHERE 1=1");
        if (name != null && !name.isEmpty()) {
            jpql.append(" AND LOWER(m.user.username) LIKE LOWER(CONCAT('%', :name, '%'))");
        }
        if (email != null && !email.isEmpty()) {
            jpql.append(" AND LOWER(m.user.email) LIKE LOWER(CONCAT('%', :email, '%'))");
        }
        if (memberType != null) {
            jpql.append(" AND m.type = :memberType");
        }
        if (associationId != null) {
            jpql.append(" AND m.association.id = :associationId");
        }
        if (isActive != null) {
            if (isActive) {
                jpql.append(" AND m.leaveDate IS NULL");
            } else {
                jpql.append(" AND m.leaveDate IS NOT NULL");
            }
        }

        jpql.append(" ORDER BY m.joinDate DESC");

        TypedQuery<Member> query = entityManager.createQuery(jpql.toString(), Member.class);
        if (name != null && !name.isEmpty()) {
            query.setParameter("name", name);
        }
        if (email != null && !email.isEmpty()) {
            query.setParameter("email", email);
        }
        if (memberType != null) {
            query.setParameter("memberType", memberType);
        }
        if (associationId != null) {
            query.setParameter("associationId", associationId);
        }

        return query.getResultList();
    }

    @Override
    public List<Member> findMembersWithAssociations(Long associationId, int limit) {
        String jpql = """
            SELECT m FROM Member m 
            WHERE m.association.id = :associationId 
            ORDER BY m.joinDate DESC
            """;
        return entityManager.createQuery(jpql, Member.class)
                .setParameter("associationId", associationId)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Object[]> findActiveMembersWithFeeCount(Long associationId) {
        String jpql = """
            SELECT m, COUNT(mf) 
            FROM Member m 
            LEFT JOIN m.fees mf 
            WHERE m.association.id = :associationId 
            AND m.leaveDate IS NULL 
            GROUP BY m.id 
            ORDER BY m.joinDate DESC
            """;
        return entityManager.createQuery(jpql, Object[].class)
                .setParameter("associationId", associationId)
                .getResultList();
    }
}