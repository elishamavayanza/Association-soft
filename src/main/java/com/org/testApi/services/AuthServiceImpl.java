package com.org.testApi.services;

import com.org.testApi.models.User;
import com.org.testApi.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private List<Observer<User>> observers = new ArrayList<>();

    // Clé secrète pour signer les JWT - CORRIGÉ
    private SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Durée de validité du token (24 heures)
    private final long tokenValidity = 24 * 60 * 60 * 1000;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        notifyObservers("AUTHENTICATION", user);
        return user;
    }

    @Override
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenValidity);

        String token = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS512) // Spécifier explicitement l'algorithme
                .compact();

        notifyObservers("TOKEN_GENERATION", user);
        return token;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // Token invalide
        }
        return false;
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Current user not found: " + username));
            notifyObservers("CURRENT_USER_RETRIEVAL", user);
            return user;
        }
        throw new RuntimeException("No authenticated user found");
    }

    @Override
    public String getCurrentUsernameFromToken(String token) {
        return extractUsername(token);
    }
    // Méthode utilitaire pour extraire les claims d'un token
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Méthode utilitaire pour extraire le username d'un token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    @Override
    public void addObserver(Observer<User> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<User> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, User entity) {
        for (Observer<User> observer : observers) {
            observer.update(event, entity);
        }
    }
}