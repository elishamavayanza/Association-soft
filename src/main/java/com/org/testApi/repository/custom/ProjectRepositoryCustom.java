package com.org.testApi.repository.custom;

import com.org.testApi.models.Project;
import java.time.LocalDate;
import java.util.List;

public interface ProjectRepositoryCustom {

    /**
     * Recherche des projets avec des filtres complexes
     * @param name nom du projet (recherche partielle)
     * @param status statut du projet
     * @param associationId ID de l'association
     * @param managerId ID du gestionnaire
     * @param startDate date de début de période
     * @param endDate date de fin de période
     * @return Liste des projets correspondant aux critères
     */
    List<Project> searchProjectsComplexQuery(
            String name,
            Project.ProjectStatus status,
            Long associationId,
            Long managerId,
            LocalDate startDate,
            LocalDate endDate);

    /**
     * Trouve les projets avec l'association et le gestionnaire associés
     * @param associationId ID de l'association
     * @param limit nombre maximum de projets à retourner
     * @return Liste des projets avec leurs associations et gestionnaires
     */
    List<Project> findProjectsWithAssociations(Long associationId, int limit);

    /**
     * Trouve les projets avec le nombre de membres et d'activités
     * @param associationId ID de l'association
     * @return Liste des projets avec le nombre de membres et d'activités
     */
    List<Object[]> findProjectsWithCounts(Long associationId);
}
