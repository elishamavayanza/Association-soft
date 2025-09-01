package com.org.testApi.controllers;

import com.org.testApi.models.Role;
import com.org.testApi.models.User;
import com.org.testApi.services.UserService;
import com.org.testApi.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserRoleController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> assignRoleToUser(
            @PathVariable Long userId,
            @PathVariable Long roleId) {

        // Récupérer l'utilisateur
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Récupérer le rôle
        Role role = roleService.getRoleById(roleId)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));

        // Attribuer le rôle à l'utilisateur
        Set<Role> roles = user.getRoles();
        if (roles == null) {
            roles = new HashSet<>();
            user.setRoles(roles);
        }
        roles.add(role);

        // Sauvegarder l'utilisateur
        User updatedUser = userService.updateUser(userId, user);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> removeRoleFromUser(
            @PathVariable Long userId,
            @PathVariable Long roleId) {

        // Récupérer l'utilisateur
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Récupérer le rôle
        Role role = roleService.getRoleById(roleId)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));

        // Retirer le rôle de l'utilisateur
        Set<Role> roles = user.getRoles();
        if (roles != null) {
            roles.remove(role);
        }

        // Sauvegarder l'utilisateur
        User updatedUser = userService.updateUser(userId, user);

        return ResponseEntity.ok(updatedUser);
    }
}