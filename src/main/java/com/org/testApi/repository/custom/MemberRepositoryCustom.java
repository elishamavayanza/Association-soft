package com.org.testApi.repository.custom;

import com.org.testApi.models.Member;
import java.time.LocalDate;
import java.util.List;

public interface MemberRepositoryCustom {

    /**
     * Recherche des membres avec des filtres complexes
     * @param name nom du membre (recherche partielle sur le nom d'utilisateur)
     * @param email email du membre (recherche partielle)
     * @param memberType type de membre
     * @param associationId ID de l'association
     * @param isActive indique si on cherche les membres actifs ou inactifs
     * @return Liste des membres correspondant aux critères
     */
    List<Member> searchMembersComplexQuery(String name, String email, Member.MemberType memberType, Long associationId, Boolean isActive);

    /**
     * Trouve les membres avec toutes les entités associées
     * @param associationId ID de l'association
     * @param limit nombre maximum de membres à retourner
     * @return Liste des membres avec leurs associations
     */
    List<Member> findMembersWithAssociations(Long associationId, int limit);

    /**
     * Trouve les membres actifs avec le nombre de cotisations payées
     * @param associationId ID de l'association
     * @return Liste des membres actifs avec le nombre de cotisations
     */
    List<Object[]> findActiveMembersWithFeeCount(Long associationId);
}
