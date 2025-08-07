package com.org.testApi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Classe de base abstraite pour toutes les entités persistantes.
 * <p>
 * Elle fournit des champs communs pour l’audit automatique via Spring Data JPA,
 * tels que la date de création, la date de modification, l’utilisateur créateur,
 * l’utilisateur modificateur, ainsi qu’un champ booléen pour marquer l’entité comme active.
 * </p>
 *
 * <p>
 * Cette classe utilise {@link AuditingEntityListener} pour remplir automatiquement
 * les champs d’audit lorsqu'une entité est persistée ou mise à jour.
 * </p>
 */
@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /**
     * Identifiant unique de l’entité.
     * Généré automatiquement par la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Date et heure de création de l'entité.
     * Ce champ est automatiquement renseigné à la création.
     */
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    /**
     * Date et heure de la dernière modification de l'entité.
     * Ce champ est automatiquement mis à jour à chaque modification.
     */
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    /**
     * Nom de l'utilisateur qui a créé l'entité.
     * Renseigné automatiquement par Spring Security si configuré.
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    /**
     * Nom de l'utilisateur ayant effectué la dernière modification.
     * Renseigné automatiquement par Spring Security si configuré.
     */
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    /**
     * Indique si l’entité est active ou non.
     * Utile pour la suppression logique (soft delete).
     */
    @Column(name = "is_active")
    private boolean active = true;
}