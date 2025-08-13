package com.org.testApi.repository.custom;

import com.org.testApi.models.User;
import java.time.LocalDateTime;
import java.util.List;

public interface UserRepositoryCustom {

    /**
     * Recherche des utilisateurs avec des filtres complexes
     * @param username nom d'utilisateur (recherche partielle)
     * @param email adresse email (recherche partielle)
     * @param firstName prénom (recherche partielle)
     * @param lastName nom de famille (recherche partielle)
     * @param roleId ID d'un rôle spécifique
     * @return Liste des utilisateurs correspondant aux critères
     */
    List<User> searchUsersComplexQuery(String username, String email, String firstName, String lastName, Integer roleId);

    /**
     * Trouve les utilisateurs avec leurs rôles
     * @param limit nombre maximum d'utilisateurs à retourner
     * @return Liste des utilisateurs avec leurs rôles
     */
    List<User> findUsersWithRoles(int limit);

    /**
     * Trouve les utilisateurs inactifs avec le nombre d'événements auxquels ils ont participé
     * @param sinceDate date de référence pour déterminer l'inactivité
     * @return Liste des utilisateurs inactifs avec le nombre d'événements
     */
    List<Object[]> findInactiveUsersWithEventCount(LocalDateTime sinceDate);
}
