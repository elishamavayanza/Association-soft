package com.org.testApi.services;

import com.org.testApi.models.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService extends ObservableService<Role> {
    List<Role> getAllRoles();
    Optional<Role> getRoleById(Long id);
    Role saveRole(Role role);
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);
    void softDeleteRole(Long id);
}
