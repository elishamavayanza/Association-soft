package com.org.testApi.repository.custom;

import com.org.testApi.models.MembershipFee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public class MembershipFeeRepositoryImpl implements MembershipFeeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MembershipFee> searchMembershipFeesComplexQuery(
            BigDecimal minAmount,
            BigDecimal maxAmount,
            MembershipFee.PaymentMethod paymentMethod,
            Long memberId,
            LocalDate startDate,
            LocalDate endDate) {
        StringBuilder jpql = new StringBuilder("SELECT mf FROM MembershipFee mf JOIN FETCH mf.member WHERE 1=1");
        if (minAmount != null) {
            jpql.append(" AND mf.amount >= :minAmount");
        }
        if (maxAmount != null) {
            jpql.append(" AND mf.amount <= :maxAmount");
        }
        if (paymentMethod != null) {
            jpql.append(" AND mf.paymentMethod = :paymentMethod");
        }
        if (memberId != null) {
            jpql.append(" AND mf.member.id = :memberId");
        }
        if (startDate != null) {
            jpql.append(" AND mf.paymentDate >= :startDate");
        }
        if (endDate != null) {
            jpql.append(" AND mf.paymentDate <= :endDate");
        }

        jpql.append(" ORDER BY mf.paymentDate DESC");

        TypedQuery<MembershipFee> query = entityManager.createQuery(jpql.toString(), MembershipFee.class);
        if (minAmount != null) {
            query.setParameter("minAmount", minAmount);
        }
        if (maxAmount != null) {
            query.setParameter("maxAmount", maxAmount);
        }
        if (paymentMethod != null) {
            query.setParameter("paymentMethod", paymentMethod);
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
    public List<MembershipFee> findMembershipFeesWithMembers(Long associationId, int limit) {
        String jpql = """
            SELECT mf FROM MembershipFee mf 
            JOIN FETCH mf.member m 
            JOIN FETCH m.user 
            JOIN FETCH m.association 
            WHERE m.association.id = :associationId 
            ORDER BY mf.paymentDate DESC
            """;
        return entityManager.createQuery(jpql, MembershipFee.class)
                .setParameter("associationId", associationId)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public BigDecimal calculateTotalAmountInPeriod(Long associationId, LocalDate startDate, LocalDate endDate) {
        String jpql = """
            SELECT SUM(mf.amount) 
            FROM MembershipFee mf 
            JOIN mf.member m 
            WHERE m.association.id = :associationId 
            AND mf.paymentDate BETWEEN :startDate AND :endDate
            """;
        return entityManager.createQuery(jpql, BigDecimal.class)
                .setParameter("associationId", associationId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();
    }
}
