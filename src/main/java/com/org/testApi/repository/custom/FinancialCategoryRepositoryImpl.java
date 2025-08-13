package com.org.testApi.repository.custom;

import com.org.testApi.models.FinancialCategory;
import com.org.testApi.models.FinancialTransaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FinancialCategoryRepositoryImpl implements FinancialCategoryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FinancialCategory> searchFinancialCategoriesComplexQuery(String name, FinancialTransaction.TransactionType type, Long associationId) {
        StringBuilder jpql = new StringBuilder("SELECT fc FROM FinancialCategory fc WHERE 1=1");
        if (name != null && !name.isEmpty()) {
            jpql.append(" AND LOWER(fc.name) LIKE LOWER(CONCAT('%', :name, '%'))");
        }
        if (type != null) {
            jpql.append(" AND fc.type = :type");
        }
        if (associationId != null) {
            jpql.append(" AND fc.association.id = :associationId");
        }

        jpql.append(" ORDER BY fc.name ASC");

        TypedQuery<FinancialCategory> query = entityManager.createQuery(jpql.toString(), FinancialCategory.class);
        if (name != null && !name.isEmpty()) {
            query.setParameter("name", name);
        }
        if (type != null) {
            query.setParameter("type", type);
        }
        if (associationId != null) {
            query.setParameter("associationId", associationId);
        }

        return query.getResultList();
    }

    @Override
    public List<FinancialCategory> findFinancialCategoriesWithTransactionCount(Long associationId) {
        String jpql = """
            SELECT fc FROM FinancialCategory fc 
            WHERE fc.association.id = :associationId 
            AND (SELECT COUNT(ft) FROM FinancialTransaction ft WHERE ft.category = fc) > 0
            ORDER BY fc.name ASC
            """;
        return entityManager.createQuery(jpql, FinancialCategory.class)
                .setParameter("associationId", associationId)
                .getResultList();
    }

    @Override
    public List<Object[]> calculateTotalAmountByCategory(Long associationId) {
        String jpql = """
            SELECT fc.name, fc.type, SUM(ft.amount) 
            FROM FinancialCategory fc 
            LEFT JOIN FinancialTransaction ft ON ft.category = fc 
            WHERE fc.association.id = :associationId 
            GROUP BY fc.id, fc.name, fc.type 
            ORDER BY fc.name ASC
            """;
        return entityManager.createQuery(jpql, Object[].class)
                .setParameter("associationId", associationId)
                .getResultList();
    }
}
