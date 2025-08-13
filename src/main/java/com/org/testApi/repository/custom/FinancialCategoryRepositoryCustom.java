package com.org.testApi.repository.custom;

import com.org.testApi.models.FinancialCategory;
import com.org.testApi.models.FinancialTransaction;
import java.util.List;

public interface FinancialCategoryRepositoryCustom {

    /**
     * Recherche des catégories financières avec des filtres complexes
     * @param name nom de la catégorie (recherche partielle)
     * @param type type de transaction (REVENUE ou EXPENSE)
     * @param associationId ID de l'association
     * @return Liste des catégories financières correspondant aux critères
     */
    List<FinancialCategory> searchFinancialCategoriesComplexQuery(String name, FinancialTransaction.TransactionType type, Long associationId);

    /**
     * Trouve les catégories financières d'une association avec le nombre de transactions associées
     * @param associationId ID de l'association
     * @return Liste des catégories avec comptage des transactions
     */
    List<FinancialCategory> findFinancialCategoriesWithTransactionCount(Long associationId);

    /**
     * Calcule le total des transactions pour chaque catégorie d'une association
     * @param associationId ID de l'association
     * @return Liste des catégories avec le montant total des transactions
     */
    List<Object[]> calculateTotalAmountByCategory(Long associationId);
}
