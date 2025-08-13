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
}
