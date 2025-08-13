package com.org.testApi.repository.custom;

import com.org.testApi.models.ProjectMember;
import java.time.LocalDate;
import java.util.List;

public interface ProjectMemberRepositoryCustom {

    /**
     * Recherche des affectations de membres à des projets avec des filtres complexes
     * @param roleInProject rôle du membre dans le projet (recherche partielle)
     * @param projectId ID du projet
     * @param memberId ID du membre
     * @param isActive indique si on cherche les affectations actives ou inactives
     * @param startDate date de début de période
     * @param endDate date de fin de période
     * @return Liste des affectations correspondant aux critères
     */
    List<ProjectMember> searchProjectMembersComplexQuery(
            String roleInProject,
            Long projectId,
            Long memberId,
            Boolean isActive,
            LocalDate startDate,
            LocalDate endDate);

    /**
     * Trouve les affectations de membres avec le projet et le membre associés
     * @param associationId ID de l'association
     * @param limit nombre maximum d'affectations à retourner
     * @return Liste des affectations avec projets et membres
     */
    List<ProjectMember> findProjectMembersWithAssociations(Long associationId, int limit);

    /**
     * Trouve les projets avec le nombre de membres actifs
     * @param associationId ID de l'association
     * @return Liste des projets avec le nombre de membres actifs
     */
    List<Object[]> findProjectsWithMemberCount(Long associationId);
}
