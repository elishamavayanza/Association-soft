package com.org.testApi.services;

import com.org.testApi.models.User;
import com.org.testApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Utiliser une requête qui charge explicitement les rôles avec l'utilisateur
        User user = userRepository.findWithRolesByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Convertir les rôles de l'utilisateur en GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        Set<com.org.testApi.models.Role> userRoles = user.getRoles();

        if (userRoles != null) {
            for (com.org.testApi.models.Role role : userRoles) {
                authorities.add(new SimpleGrantedAuthority(role.getName().name()));
            }
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
