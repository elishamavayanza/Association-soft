package com.org.testApi.repository.custom;

import com.org.testApi.models.Loan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public class LoanRepositoryImpl implements LoanRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Loan> searchLoansComplexQuery(Long memberId, BigDecimal minAmount, BigDecimal maxAmount,
                                              Loan.LoanStatus status, LocalDate startDate, LocalDate endDate) {
        StringBuilder jpql = new StringBuilder("SELECT l FROM Loan l JOIN FETCH l.member WHERE 1=1");

        if (memberId != null) {
            jpql.append(" AND l.member.id = :memberId");
        }

        if (minAmount != null) {
            jpql.append(" AND l.amount >= :minAmount");
        }

        if (maxAmount != null) {
            jpql.append(" AND l.amount <= :maxAmount");
        }

        if (status != null) {
            jpql.append(" AND l.status = :status");
        }

        if (startDate != null) {
            jpql.append(" AND l.dueDate >= :startDate");
        }

        if (endDate != null) {
            jpql.append(" AND l.dueDate <= :endDate");
        }

        jpql.append(" ORDER BY l.dueDate ASC");

        TypedQuery<Loan> query = entityManager.createQuery(jpql.toString(), Loan.class);

        if (memberId != null) {
            query.setParameter("memberId", memberId);
        }

        if (minAmount != null) {
            query.setParameter("minAmount", minAmount);
        }

        if (maxAmount != null) {
            query.setParameter("maxAmount", maxAmount);
        }

        if (status != null) {
            query.setParameter("status", status);
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
    public BigDecimal calculateTotalLoansForMember(Long memberId) {
        String jpql = "SELECT COALESCE(SUM(l.amount), 0) FROM Loan l WHERE l.member.id = :memberId";
        TypedQuery<BigDecimal> query = entityManager.createQuery(jpql, BigDecimal.class);
        query.setParameter("memberId", memberId);
        return query.getSingleResult();
    }

    @Override
    public List<Loan> findLoansWithMembers(int limit) {
        String jpql = "SELECT l FROM Loan l JOIN FETCH l.member m JOIN FETCH m.user JOIN FETCH m.association";
        TypedQuery<Loan> query = entityManager.createQuery(jpql, Loan.class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Object[]> findOverdueLoansWithDaysOverdue() {
        String jpql = "SELECT l, DATEDIFF(CURRENT_DATE, l.dueDate) FROM Loan l " +
                "WHERE l.dueDate < CURRENT_DATE AND l.status != 'REPAID'";
        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        return query.getResultList();
    }
}
