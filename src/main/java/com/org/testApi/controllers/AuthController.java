package com.org.testApi.controllers;

import com.org.testApi.models.User;
import com.org.testApi.payload.LoginRequest;
import com.org.testApi.services.AuthService;
import com.org.testApi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.org.testApi.payload.RegistrationRequest;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "Gestion de l'authentification et de l'inscription des utilisateurs")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    @Operation(summary = "Authentifier un utilisateur", description = "Permet à un utilisateur de s'authentifier et d'obtenir un jeton JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentification réussie, jeton JWT retourné",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(
                                    name = "Exemple de token",
                                    summary = "Exemple de réponse avec token JWT",
                                    value = "{\n  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\"\n}"
                            ))}),
            @ApiResponse(responseCode = "401", description = "Échec de l'authentification"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<String> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Informations de connexion de l'utilisateur",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(
                                    name = "Exemple de connexion",
                                    summary = "Exemple de demande de connexion",
                                    value = "{\n  \"username\": \"muhongo\",\n  \"password\": \"Ma1234\"\n}"
                            )
                    )
            ) @Valid @RequestBody LoginRequest loginRequest) {
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
    @Operation(summary = "Inscrire un nouvel utilisateur", description = "Permet d'inscrire un nouvel utilisateur dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur enregistré avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Données invalides ou utilisateur déjà existant"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Informations de l'utilisateur à inscrire",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegistrationRequest.class),
                            examples = @ExampleObject(
                                    name = "Exemple d'utilisateur",
                                    summary = "Exemple de création d'utilisateur",
                                    value = "{\n" +
                                            "  \"username\": \"elishamavayanza\",\n" +
                                            "  \"email\": \"elishama.vayanza@example.com\",\n" +
                                            "  \"password\": \"motdepasse123\",\n" +
                                            "  \"firstName\": \"Elishama\",\n" +
                                            "  \"lastName\": \"VAYANZA\",\n" +
                                            "  \"phoneNumber\": \"+234991471988\"\n" +
                                            "}"
                            )
                    )
            ) @Valid @RequestBody RegistrationRequest registrationRequest) {
        try {
            // Check if user already exists
            if (userService.getAllUsers().stream()
                    .anyMatch(u -> u.getUsername().equals(registrationRequest.getUsername()) || 
                                   u.getEmail().equals(registrationRequest.getEmail()))) {
                return ResponseEntity.badRequest().body("User with this username or email already exists");
            }
            
            // Convert RegistrationRequest to User
            User user = new User();
            user.setUsername(registrationRequest.getUsername());
            user.setEmail(registrationRequest.getEmail());
            user.setPassword(registrationRequest.getPassword());
            user.setFirstName(registrationRequest.getFirstName());
            user.setLastName(registrationRequest.getLastName());
            user.setPhoneNumber(registrationRequest.getPhoneNumber());

            // Save the user
            User registeredUser = userService.saveUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            logger.error("Error during user registration: ", e);
            return ResponseEntity.status(500).body("Error during registration: " + e.getMessage());
        }
    }
}