package com.org.testApi.controllers;

import com.org.testApi.models.User;
import com.org.testApi.payload.LoginRequest;
import com.org.testApi.services.AuthService;
import com.org.testApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
            String token = authService.generateToken(user);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            logger.error("Authentication failed for username: " + loginRequest.getUsername(), e);
            return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            // Vérifier si l'utilisateur existe déjà
            if (userService.getAllUsers().stream()
                    .anyMatch(u -> u.getUsername().equals(user.getUsername()) || u.getEmail().equals(user.getEmail()))) {
                return ResponseEntity.badRequest().build();
            }

            // Dans une vraie application, vous voudrez hasher le mot de passe
            // et effectuer d'autres vérifications
            User registeredUser = userService.saveUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
