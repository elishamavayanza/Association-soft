package com.org.testApi.controllers;

import com.org.testApi.dto.UserDTO;
import com.org.testApi.models.User;
import com.org.testApi.payload.UserPayload;
import com.org.testApi.services.UserService;
import com.org.testApi.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Créer un nouvel utilisateur",
            description = "Crée un nouvel utilisateur avec les détails fournis"
    )
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
                                    "  \"username\": \"janesmith\",\n" +
                                    "  \"email\": \"jane.smith@example.com\",\n" +
                                    "  \"firstName\": \"Jane\",\n" +
                                    "  \"lastName\": \"Smith\",\n" +
                                    "  \"phoneNumber\": \"+1987654321\",\n" +
                                    "  \"enabled\": true,\n" +
                                    "  \"roles\": [\n" +
                                    "    {\n" +
                                    "      \"id\": 1,\n" +
                                    "      \"name\": \"ROLE_MEMBER\",\n" +
                                    "      \"description\": \"Rôle membre\"\n" +
                                    "    }\n" +
                                    "  ]\n" +
                                    "}"
                    )
            )
    )
    @PostMapping("/extended")
    public ResponseEntity<User> createUserWithRoles(@Valid @RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
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
                                    "  \"username\": \"mikejohnson\",\n" +
                                    "  \"email\": \"mike.johnson@example.com\",\n" +
                                    "  \"password\": \"motdepassesecurise\",\n" +
                                    "  \"enabled\": true,\n" +
                                    "  \"roleId\": 2\n" +
                                    "}"
                    )
            )
    )
    @PostMapping("/payload")
    public ResponseEntity<User> createUserFromPayload(@Valid @RequestBody UserPayload payload) {
        User user = userMapper.toNewEntityFromPayload(payload);
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
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
    public ResponseEntity<User> updateUserWithPayload(@PathVariable Long id, @Valid @RequestBody UserPayload payload) {
        return userService.getUserById(id)
                .map(user -> {
                    userMapper.updateEntityFromPayload(payload, user);
                    User updatedUser = userService.updateUser(id, user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<Void> softDeleteUser(@PathVariable Long id) {
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Recherche des utilisateurs avec des filtres complexes.
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Integer roleId) {
        List<User> users = userService.searchUsersComplexQuery(username, email, firstName, lastName, roleId);
        return ResponseEntity.ok(users);
    }

}
