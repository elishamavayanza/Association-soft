package com.org.testApi.controllers;

import com.org.testApi.dto.UserDTO;
import com.org.testApi.mapper.UserMapper;
import com.org.testApi.models.Role;
import com.org.testApi.models.User;
import com.org.testApi.payload.UserPayload;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Utilisateur", description = "Gestion des utilisateurs")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    // =============================================
    // ENDPOINTS DE TEST
    // =============================================

    @PostMapping("/test")
    @Operation(summary = "Endpoint de test pour vérifier la désérialisation")
    public ResponseEntity<String> testPayload(@RequestBody java.util.Map<String, Object> payload) {
        logger.info("Test payload received: {}", payload);
        return ResponseEntity.ok("Received: " + payload.toString());
    }

    @PostMapping("/test-payload")
    @Operation(summary = "Endpoint de test pour TestPayload")
    public ResponseEntity<String> testSpecificPayload(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Test payload",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.org.testApi.payload.TestPayload.class)
                    )
            ) @org.springframework.web.bind.annotation.RequestBody com.org.testApi.payload.TestPayload payload) {
        logger.info("TestPayload received: username='{}', email='{}', password='{}'",
                payload.getUsername(), payload.getEmail(), payload.getPassword());
        return ResponseEntity.ok("Received: username=" + payload.getUsername());
    }

    // =============================================
    // OPÉRATIONS CRUD PRINCIPALES
    // =============================================

    @GetMapping
    @Operation(
            summary = "Récupérer tous les utilisateurs",
            description = "Retourne une liste de tous les utilisateurs enregistrés dans le système"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des utilisateurs récupérée avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Récupérer un utilisateur par ID",
            description = "Retourne un utilisateur spécifique en fonction de son identifiant"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID de l'utilisateur à récupérer") @PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Créer un nouvel utilisateur",
            description = "Crée un nouvel utilisateur avec les détails fournis"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur créé avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objet utilisateur à créer",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class),
                    examples = @ExampleObject(
                            name = "Exemple d'utilisateur",
                            summary = "Exemple de création d'utilisateur",
                            value = """
                                {
                                  "username": "johndoe",
                                  "email": "john.doe@example.com",
                                  "password": "motdepasse123",
                                  "firstName": "John",
                                  "lastName": "Doe",
                                  "phoneNumber": "+1234567890"
                                }
                                """
                    )
            )
    )
    public ResponseEntity<User> createUser(@Valid @org.springframework.web.bind.annotation.RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/extended")
    @Operation(
            summary = "Créer un nouvel utilisateur avec des rôles",
            description = "Crée un nouvel utilisateur avec les détails fournis et les rôles associés"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur créé avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "DTO utilisateur avec rôles à créer",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class),
                    examples = @ExampleObject(
                            name = "Exemple d'utilisateur avec rôles",
                            summary = "Exemple de création d'utilisateur avec rôles",
                            value = """
                                {
                                  "username": "elishamavayanza",
                                  "email": "elishama.vayanza@example.com",
                                  "password": "motdepasse123",
                                  "firstName": "Elishama",
                                  "lastName": "VAYANZA",
                                  "phoneNumber": "+234991471988",
                                  "enabled": true,
                                  "roles": [
                                    {
                                      "id": 2,
                                      "name": "ROLE_MEMBER",
                                      "description": "Membre standard"
                                    }
                                  ]
                                }
                                """
                    )
            )
    )
    public ResponseEntity<User> createUserWithRoles(@Valid @org.springframework.web.bind.annotation.RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        // Copier manuellement le mot de passe si présent
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(userDTO.getPassword());
        }
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            user.setRoles(userMapper.toRoleEntitySet(userDTO.getRoles()));
        }
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/payload")
    @Operation(summary = "Créer un utilisateur à partir d'un payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<?> createUserFromPayload(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload utilisateur à créer",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserPayload.class),
                            examples = @ExampleObject(
                                    name = "Exemple de payload utilisateur",
                                    summary = "Exemple de création de payload utilisateur",
                                    value = """
                                        {
                                          "username": "elishamavayanza",
                                          "email": "elishama.vayanza@example.com",
                                          "password": "motdepasse123",
                                          "firstName": "Elishama",
                                          "lastName": "VAYANZA",
                                          "phoneNumber": "+234991471988",
                                          "enabled": true,
                                          "roleId": 2
                                        }
                                        """
                            )
                    )
            ) @Valid @org.springframework.web.bind.annotation.RequestBody UserPayload payload) {
        try {
            logger.info("Received payload: username='{}', email='{}', password='{}'",
                    payload.getUsername(), payload.getEmail(), payload.getPassword());

            User user = userMapper.toNewEntityFromPayload(payload);

            // Log what was mapped to the user entity
            logger.info("Mapped user: username='{}', email='{}', password='{}'",
                    user.getUsername(), user.getEmail(), user.getPassword() != null ? "****" : "NULL");

            // Gérer le rôle si roleId est fourni
            if (payload.getRoleId() != null) {
                Role role = userMapper.toRoleEntity(payload.getRoleId());
                if (role != null) {
                    user.setRoles(new HashSet<>());
                    user.getRoles().add(role);
                }
            }
            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            logger.error("Error creating user from payload: ", e);
            return ResponseEntity.status(500).body("Error creating user: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Mettre à jour un utilisateur",
            description = "Met à jour un utilisateur existant avec les données fournies"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<User> updateUser(
            @Parameter(description = "ID de l'utilisateur à mettre à jour") @PathVariable Long id,
            @Valid @org.springframework.web.bind.annotation.RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    @Operation(
            summary = "Mettre à jour un utilisateur avec payload",
            description = "Met à jour un utilisateur existant en utilisant un objet payload"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<User> updateUserWithPayload(
            @Parameter(description = "ID de l'utilisateur à mettre à jour") @PathVariable Long id,
            @Valid @org.springframework.web.bind.annotation.RequestBody UserPayload payload) {
        return userService.getUserById(id)
                .map(user -> {
                    userMapper.updateEntityFromPayload(payload, user);
                    User updatedUser = userService.updateUser(id, user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un utilisateur",
            description = "Supprime définitivement un utilisateur"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Utilisateur supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID de l'utilisateur à supprimer") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    @Operation(
            summary = "Supprimer logiquement un utilisateur",
            description = "Marque un utilisateur comme supprimé sans le retirer de la base de données"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Utilisateur supprimé logiquement avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> softDeleteUser(
            @Parameter(description = "ID de l'utilisateur à supprimer logiquement") @PathVariable Long id) {
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(
            summary = "Rechercher des utilisateurs",
            description = "Recherche des utilisateurs avec des filtres complexes"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Résultats de recherche récupérés avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<User>> searchUsers(
            @Parameter(description = "Nom d'utilisateur (optionnel)") @RequestParam(required = false) String username,
            @Parameter(description = "Adresse email (optionnel)") @RequestParam(required = false) String email,
            @Parameter(description = "Prénom (optionnel)") @RequestParam(required = false) String firstName,
            @Parameter(description = "Nom de famille (optionnel)") @RequestParam(required = false) String lastName,
            @Parameter(description = "ID du rôle (optionnel)") @RequestParam(required = false) Integer roleId) {
        List<User> users = userService.searchUsersComplexQuery(username, email, firstName, lastName, roleId);
        return ResponseEntity.ok(users);
    }

    // =============================================
    // GESTION DES EXCEPTIONS
    // =============================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
            logger.info("Validation error - Field: {}, Message: {}, Rejected Value: {}",
                    error.getField(), error.getDefaultMessage(), error.getRejectedValue());
        });
        return ResponseEntity.badRequest().body("Données invalides : " + errors.toString());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body("Erreur de validation : " + ex.getMessage());
    }
}