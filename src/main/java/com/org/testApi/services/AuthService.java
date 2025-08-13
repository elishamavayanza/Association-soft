package com.org.testApi.services;

import com.org.testApi.models.User;

public interface AuthService extends ObservableService<User> {
    User authenticateUser(String username, String password);
    String generateToken(User user);
    boolean validateToken(String token);
    User getCurrentUser();
}
