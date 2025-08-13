package com.org.testApi.services;

import com.org.testApi.models.Role;
import com.org.testApi.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    private List<Observer<Role>> observers = new ArrayList<>();

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id.intValue());
    }

    @Override
    public Role saveRole(Role role) {
        Role savedRole = roleRepository.save(role);
        notifyObservers("SAVE", savedRole);
        return savedRole;
    }

    @Override
    public Role updateRole(Long id, Role role) {
        if (roleRepository.existsById(id.intValue())) {
            role.setId(id.intValue());
            Role updatedRole = roleRepository.save(role);
            notifyObservers("UPDATE", updatedRole);
            return updatedRole;
        }
        throw new RuntimeException("Role not found with id: " + id);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id.intValue()).orElse(null);
        roleRepository.deleteById(id.intValue());
        if (role != null) {
            notifyObservers("DELETE", role);
        }
    }

    @Override
    public void softDeleteRole(Long id) {
        Role role = roleRepository.findById(id.intValue()).orElse(null);
        roleRepository.softDelete(id.intValue());
        if (role != null) {
            notifyObservers("SOFT_DELETE", role);
        }
    }

    @Override
    public void addObserver(Observer<Role> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Role> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, Role entity) {
        for (Observer<Role> observer : observers) {
            observer.update(event, entity);
        }
    }
}
