package com.org.testApi.controllers;

import com.org.testApi.models.Role;
import com.org.testApi.payload.RolePayload;
import com.org.testApi.services.RoleService;
import com.org.testApi.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMapper roleMapper;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role savedRole = roleService.saveRole(role);
        return ResponseEntity.ok(savedRole);
    }

    @PostMapping("/payload")
    public ResponseEntity<Role> createRoleFromPayload(@RequestBody RolePayload payload) {
        Role role = roleMapper.toEntityFromPayload(payload);
        Role savedRole = roleService.saveRole(role);
        return ResponseEntity.ok(savedRole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        try {
            Role updatedRole = roleService.updateRole(id, role);
            return ResponseEntity.ok(updatedRole);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/payload")
    public ResponseEntity<Role> updateRoleWithPayload(@PathVariable Long id, @RequestBody RolePayload payload) {
        return roleService.getRoleById(id)
                .map(role -> {
                    roleMapper.updateEntityFromPayload(payload, role);
                    Role updatedRole = roleService.updateRole(id, role);
                    return ResponseEntity.ok(updatedRole);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<Void> softDeleteRole(@PathVariable Long id) {
        roleService.softDeleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
