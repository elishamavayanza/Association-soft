package com.org.testApi.services;

import com.org.testApi.models.User;
import com.org.testApi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private List<Observer<User>> observers = new ArrayList<>();

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        logger.info("Fetching user by ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            logger.info("User found: {}", user.get().getUsername());
        } else {
            logger.warn("User not found with ID: {}", id);
        }
        return user;
    }

    @Override
    public User saveUser(User user) {
        // Hasher le mot de passe avant de l'enregistrer
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Correction ici
        }
        User savedUser = userRepository.save(user);
        notifyObservers("SAVE", savedUser);
        return savedUser;
    }

    @Override
    public User updateUser(Long id, User user) {
        logger.info("Updating user with ID: {}", id);
        if (userRepository.existsById(id)) {
            user.setId(id);

            // Toujours récupérer le mot de passe existant depuis la base de données
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.error("User not found with id: {}", id);
                        return new RuntimeException("User not found with id: " + id);
                    });
            user.setPassword(existingUser.getPassword());

            logger.info("Saving updated user");
            User updatedUser = userRepository.save(user);
            logger.info("User updated successfully");
            notifyObservers("UPDATE", updatedUser);
            return updatedUser;
        }
        logger.error("User not found with id: {}", id);
        throw new RuntimeException("User not found with id: " + id);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        userRepository.deleteById(id);
        if (user != null) {
            notifyObservers("DELETE", user);
        }
    }

    @Override
    public void softDeleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        userRepository.softDelete(id);
        if (user != null) {
            notifyObservers("SOFT_DELETE", user);
        }
    }

    @Override
    public List<User> searchUsersComplexQuery(String username, String email, String firstName, String lastName, Integer roleId) {
        return userRepository.searchUsersComplexQuery(username, email, firstName, lastName, roleId);
    }

    @Override
    public void addObserver(Observer<User> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<User> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, User entity) {
        for (Observer<User> observer : observers) {
            observer.update(event, entity);
        }
    }
}