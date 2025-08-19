package com.org.testApi.services;

import com.org.testApi.models.User;
import com.org.testApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private List<Observer<User>> observers = new ArrayList<>();

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User saveUser(User user) {
        User savedUser = userRepository.save(user);
        notifyObservers("SAVE", savedUser);
        return savedUser;
    }

    @Override
    public User updateUser(Long id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            User updatedUser = userRepository.save(user);
            notifyObservers("UPDATE", updatedUser);
            return updatedUser;
        }
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
