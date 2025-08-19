package com.org.testApi.services;

import com.org.testApi.models.User;
import java.util.List;
import java.util.Optional;

public interface UserService extends ObservableService<User> {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User saveUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    void softDeleteUser(Long id);

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
}
