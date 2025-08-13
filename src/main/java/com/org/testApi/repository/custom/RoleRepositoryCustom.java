package com.org.testApi.repository.custom;

import com.org.testApi.models.Role;
import java.util.List;

public interface RoleRepositoryCustom {

    /**
     * Recherche des rôles avec des filtres complexes
     * @param name nom du rôle (recherche partielle)
     * @param description description du rôle (recherche partielle)
     * @return Liste des rôles correspondant aux critères
     */
    List<Role> searchRolesComplexQuery(String name, String description);

    /**
     * Trouve les rôles avec le nombre d'utilisateurs associés
     * @param limit nombre maximum de rôles à retourner
     * @return Liste des rôles avec le nombre d'utilisateurs
     */
    List<Object[]> findRolesWithUserCount(int limit);

    /**
     * Trouve les rôles qui ne sont pas utilisés par des utilisateurs
     * @return Liste des rôles inutilisés
     */
    List<Role> findUnusedRoles();
}
