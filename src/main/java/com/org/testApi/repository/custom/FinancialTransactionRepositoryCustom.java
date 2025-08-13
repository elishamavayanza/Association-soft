package com.org.testApi.repository.custom;

import com.org.testApi.models.FinancialTransaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FinancialTransactionRepositoryCustom {

    /**
     * Recherche des transactions financières avec des filtres complexes
     * @param type type de transaction (INCOME ou EXPENSE)
     * @param minAmount montant minimum
     * @param maxAmount montant maximum
     * @param startDate date de début
     * @param endDate date de fin
     * @param associationId ID de l'association
     * @return Liste des transactions financières correspondant aux critères
     */
    List<FinancialTransaction> searchFinancialTransactionsComplexQuery(
            FinancialTransaction.TransactionType type,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            LocalDate startDate,
            LocalDate endDate,
            Long associationId);

    /**
     * Trouve les transactions financières avec toutes les entités associées
     * @param associationId ID de l'association
     * @param limit nombre maximum de transactions à retourner
     * @return Liste des transactions avec leurs associations
     */
    List<FinancialTransaction> findFinancialTransactionsWithAssociations(Long associationId, int limit);

    /**
     * Calcule le solde (revenus - dépenses) pour une période donnée
     * @param associationId ID de l'association
     * @param startDate date de début
     * @param endDate date de fin
     * @return Solde pour la période
     */
    BigDecimal calculateBalanceForPeriod(Long associationId, LocalDate startDate, LocalDate endDate);
}
