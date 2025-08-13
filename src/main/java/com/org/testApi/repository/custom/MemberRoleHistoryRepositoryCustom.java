package com.org.testApi.repository.custom;

import com.org.testApi.models.MemberRoleHistory;
import java.time.LocalDate;
import java.util.List;

public interface MemberRoleHistoryRepositoryCustom {

    /**
     * Recherche de l'historique des rôles avec des filtres complexes
     * @param role nom du rôle (recherche partielle)
     * @param memberId ID du membre
     * @param startDate date de début de période
     * @param endDate date de fin de période
     * @param isActive indique si on cherche les rôles actifs (endDate null) ou inactifs
     * @return Liste des historiques de rôles correspondant aux critères
     */
    List<MemberRoleHistory> searchMemberRoleHistoryComplexQuery(String role, Long memberId, LocalDate startDate, LocalDate endDate, Boolean isActive);

    /**
     * Trouve l'historique des rôles avec le membre associé
     * @param associationId ID de l'association
     * @param limit nombre maximum d'éléments à retourner
     * @return Liste des historiques de rôles avec les membres
     */
    List<MemberRoleHistory> findMemberRoleHistoryWithMembers(Long associationId, int limit);

    /**
     * Trouve les membres avec leurs rôles actuels
     * @param associationId ID de l'association
     * @return Liste des membres avec leurs rôles actuels
     */
    List<Object[]> findMembersWithCurrentRoles(Long associationId);
}
