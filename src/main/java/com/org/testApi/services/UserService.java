package com.org.testApi.services;

import com.org.testApi.models.Role;
import com.org.testApi.models.User;
import java.util.List;
import java.util.Optional;

public interface UserService extends ObservableService<User> {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    Optional<Role> getRoleById(Long id);
    Optional<Role> getRoleByName(Role.ERole name);
    User saveUser(User user);
    Role saveRole(Role role);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    void softDeleteUser(Long id);
    List<User> searchUsersComplexQuery(String username, String email, String firstName, String lastName, Integer roleId);
}