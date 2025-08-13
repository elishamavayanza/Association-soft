package com.org.testApi.repository.custom;

import com.org.testApi.models.MembershipFee;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface MembershipFeeRepositoryCustom {

    /**
     * Recherche des cotisations avec des filtres complexes
     * @param minAmount montant minimum
     * @param maxAmount montant maximum
     * @param paymentMethod mode de paiement
     * @param memberId ID du membre
     * @param startDate date de début de période
     * @param endDate date de fin de période
     * @return Liste des cotisations correspondant aux critères
     */
    List<MembershipFee> searchMembershipFeesComplexQuery(
            BigDecimal minAmount,
            BigDecimal maxAmount,
            MembershipFee.PaymentMethod paymentMethod,
            Long memberId,
            LocalDate startDate,
            LocalDate endDate);

    /**
     * Trouve les cotisations avec le membre associé
     * @param associationId ID de l'association
     * @param limit nombre maximum de cotisations à retourner
     * @return Liste des cotisations avec les membres
     */
    List<MembershipFee> findMembershipFeesWithMembers(Long associationId, int limit);

    /**
     * Calcule le total des cotisations par période
     * @param associationId ID de l'association
     * @param startDate date de début de période
     * @param endDate date de fin de période
     * @return Montant total des cotisations pour la période
     */
    BigDecimal calculateTotalAmountInPeriod(Long associationId, LocalDate startDate, LocalDate endDate);
}
