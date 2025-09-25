package com.org.testApi.config;

import com.org.testApi.models.Role;
import com.org.testApi.models.User;
import com.org.testApi.repository.RoleRepository;
import com.org.testApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Créer les rôles par défaut s'ils n'existent pas
        if (!roleRepository.existsByName(Role.ERole.ROLE_ADMIN)) {
            Role adminRole = new Role();
            adminRole.setName(Role.ERole.ROLE_ADMIN);
            roleRepository.save(adminRole);
        }

        if (!roleRepository.existsByName(Role.ERole.ROLE_MEMBER)) {
            Role memberRole = new Role();
            memberRole.setName(Role.ERole.ROLE_MEMBER);
            roleRepository.save(memberRole);
        }

        // Créer un utilisateur admin par défaut s'il n'existe pas
        if (!userRepository.existsByUsername("admin")) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@association.org");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");

            Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN).orElse(null);
            Set<Role> roles = new HashSet<>();
            if (adminRole != null) {
                roles.add(adminRole);
            }
            adminUser.setRoles(roles);

            userRepository.save(adminUser);
        }
    }
}