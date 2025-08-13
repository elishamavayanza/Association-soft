package com.org.testApi.repository.custom;

import com.org.testApi.models.FinancialTransaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public class FinancialTransactionRepositoryImpl implements FinancialTransactionRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FinancialTransaction> searchFinancialTransactionsComplexQuery(
            FinancialTransaction.TransactionType type,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            LocalDate startDate,
            LocalDate endDate,
            Long associationId) {
        StringBuilder jpql = new StringBuilder("SELECT ft FROM FinancialTransaction ft WHERE 1=1");
        if (type != null) {
            jpql.append(" AND ft.type = :type");
        }
        if (minAmount != null) {
            jpql.append(" AND ft.amount >= :minAmount");
        }
        if (maxAmount != null) {
            jpql.append(" AND ft.amount <= :maxAmount");
        }
        if (startDate != null) {
            jpql.append(" AND ft.transactionDate >= :startDate");
        }
        if (endDate != null) {
            jpql.append(" AND ft.transactionDate <= :endDate");
        }
        if (associationId != null) {
            jpql.append(" AND ft.association.id = :associationId");
        }

        jpql.append(" ORDER BY ft.transactionDate DESC");

        TypedQuery<FinancialTransaction> query = entityManager.createQuery(jpql.toString(), FinancialTransaction.class);
        if (type != null) {
            query.setParameter("type", type);
        }
        if (minAmount != null) {
            query.setParameter("minAmount", minAmount);
        }
        if (maxAmount != null) {
            query.setParameter("maxAmount", maxAmount);
        }
        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }
        if (associationId != null) {
            query.setParameter("associationId", associationId);
        }

        return query.getResultList();
    }

    @Override
    public List<FinancialTransaction> findFinancialTransactionsWithAssociations(Long associationId, int limit) {
        String jpql = """
            SELECT ft FROM FinancialTransaction ft 
            LEFT JOIN FETCH ft.association 
            LEFT JOIN FETCH ft.category 
            LEFT JOIN FETCH ft.activity 
            LEFT JOIN FETCH ft.project 
            WHERE ft.association.id = :associationId 
            ORDER BY ft.transactionDate DESC
            """;
        return entityManager.createQuery(jpql, FinancialTransaction.class)
                .setParameter("associationId", associationId)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public BigDecimal calculateBalanceForPeriod(Long associationId, LocalDate startDate, LocalDate endDate) {
        String jpql = """
            SELECT SUM(CASE WHEN ft.type = 'INCOME' THEN ft.amount ELSE ft.amount * -1 END) 
            FROM FinancialTransaction ft 
            WHERE ft.association.id = :associationId 
            AND ft.transactionDate BETWEEN :startDate AND :endDate
            """;
        return entityManager.createQuery(jpql, BigDecimal.class)
                .setParameter("associationId", associationId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();
    }
}
