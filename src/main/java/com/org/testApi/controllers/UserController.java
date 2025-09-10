package com.org.testApi.controllers;

import com.org.testApi.dto.UserDTO;
import com.org.testApi.models.Role;
import com.org.testApi.models.User;
import com.org.testApi.payload.UserPayload;
import com.org.testApi.services.UserService;
import com.org.testApi.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Utilisateur", description = "Gestion des utilisateurs")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    @Operation(
            summary = "Récupérer tous les utilisateurs",
            description = "Retourne une liste de tous les utilisateurs enregistrés dans le système"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des utilisateurs récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
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
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID de l'utilisateur à récupérer") @PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Créer un nouvel utilisateur",
            description = "Crée un nouvel utilisateur avec les détails fournis"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur créé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @RequestBody(
            description = "Objet utilisateur à créer",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class),
                    examples = @ExampleObject(
                            name = "Exemple d'utilisateur",
                            summary = "Exemple de création d'utilisateur",
                            value = "{\n" +
                                    "  \"username\": \"johndoe\",\n" +
                                    "  \"email\": \"john.doe@example.com\",\n" +
                                    "  \"password\": \"motdepasse123\",\n" +
                                    "  \"firstName\": \"John\",\n" +
                                    "  \"lastName\": \"Doe\",\n" +
                                    "  \"phoneNumber\": \"+1234567890\"\n" +
                                    "}"
                    )
            )
    )
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @Operation(
            summary = "Créer un nouvel utilisateur avec des rôles",
            description = "Crée un nouvel utilisateur avec les détails fournis et les rôles associés"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur créé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @RequestBody(
            description = "DTO utilisateur avec rôles à créer",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class),
                    examples = @ExampleObject(
                            name = "Exemple d'utilisateur avec rôles",
                            summary = "Exemple de création d'utilisateur avec rôles",
                            value = "{\n" +
                                    "  \"username\": \"elishamavayanza\",\n" +
                                    "  \"email\": \"elishama.vayanza@example.com\",\n" +
                                    "  \"password\": \"motdepasse123\",\n" +
                                    "  \"firstName\": \"Elishama\",\n" +
                                    "  \"lastName\": \"VAYANZA\",\n" +
                                    "  \"phoneNumber\": \"+234991471988\",\n" +
                                    "  \"enabled\": true,\n" +
                                    "  \"roles\": [\n" +
                                    "    {\n" +
                                    "      \"id\": 2,\n" +
                                    "      \"name\": \"ROLE_MEMBER\",\n" +
                                    "      \"description\": \"Membre standard\"\n" +
                                    "    }\n" +
                                    "  ]\n" +
                                    "}"
                    )
            )
    )
    @PostMapping("/extended")
    public ResponseEntity<User> createUserWithRoles(@Valid @RequestBody UserDTO userDTO) {
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


    @Operation(
            summary = "Créer un nouvel utilisateur à partir d'un payload",
            description = "Crée un nouvel utilisateur en utilisant la structure UserPayload"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur créé avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @RequestBody(
            description = "Payload utilisateur à créer",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserPayload.class),
                    examples = @ExampleObject(
                            name = "Exemple de payload utilisateur",
                            summary = "Exemple de création de payload utilisateur",
                            value = "{\n" +
                                    "  \"username\": \"elishamavayanza\",\n" +
                                    "  \"email\": \"elishama.vayanza@example.com\",\n" +
                                    "  \"password\": \"motdepasse123\",\n" +
                                    "  \"firstName\": \"Elishama\",\n" +
                                    "  \"lastName\": \"VAYANZA\",\n" +
                                    "  \"phoneNumber\": \"+234991471988\",\n" +
                                    "  \"enabled\": true,\n" +
                                    "  \"roleId\": 2\n" +
                                    "}"
                    )
            )
    )
    @PostMapping("/payload")
    public ResponseEntity<User> createUserFromPayload(@Valid @RequestBody UserPayload payload) {
        User user = userMapper.toNewEntityFromPayload(payload);
        // Gérer le rôle si roleId est fourni
        if (payload.getRoleId() != null) {
            Role role = userMapper.toRoleEntity(payload.getRoleId());
            if (role != null) {
                user.getRoles().add(role);
            }
        }
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }
    @PutMapping("/{id}")
    @Operation(
            summary = "Mettre à jour un utilisateur",
            description = "Met à jour un utilisateur existant avec les données fournies"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<User> updateUser(
            @Parameter(description = "ID de l'utilisateur à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données de mise à jour de l'utilisateur") @Valid @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; "));
        return ResponseEntity.badRequest().body("Données invalides : " + errors.toString());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body("Erreur de validation : " + ex.getMessage());
    }

    @PutMapping("/{id}/payload")
    @Operation(
            summary = "Mettre à jour un utilisateur avec payload",
            description = "Met à jour un utilisateur existant en utilisant un objet payload"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<User> updateUserWithPayload(
            @Parameter(description = "ID de l'utilisateur à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Données du payload pour mettre à jour l'utilisateur") @Valid @RequestBody UserPayload payload) {
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

    /**
     * Recherche des utilisateurs avec des filtres complexes.
     */
    @GetMapping("/search")
    @Operation(
            summary = "Rechercher des utilisateurs",
            description = "Recherche des utilisateurs avec des filtres complexes"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Résultats de recherche récupérés avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
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

}
