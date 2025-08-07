package com.org.testApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Configuration Spring permettant d’activer l’audit JPA.
 * <p>
 * Cette configuration est utilisée pour renseigner automatiquement les champs
 * d'audit (comme {@code createdBy}, {@code modifiedBy}) dans les entités auditées,
 * en se basant sur l’utilisateur actuellement authentifié.
 * </p>
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

    /**
     * Fournit un {@link AuditorAware} qui détermine l'utilisateur actuellement connecté.
     * <p>
     * Cette méthode est utilisée par Spring Data JPA pour injecter automatiquement
     * le nom de l’utilisateur dans les champs audités des entités.
     * </p>
     *
     * @return un {@link AuditorAware} contenant le nom de l'utilisateur authentifié,
     *         ou {@code "system"} si aucun utilisateur n'est authentifié.
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            // Si vous utilisez Spring Security
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                return Optional.of(authentication.getName());
            }
            return Optional.of("system"); // Valeur par défaut si non authentifié
        };
    }
}