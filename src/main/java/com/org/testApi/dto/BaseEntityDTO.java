package com.org.testApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Classe DTO de base abstraite pour tous les DTO.
 * <p>
 * Elle fournit des champs communs pour l'audit,
 * tels que la date de création, la date de modification, l'utilisateur créateur,
 * l'utilisateur modificateur, ainsi qu'un champ booléen pour marquer l'entité comme active.
 * </p>
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntityDTO {

    /**
     * Identifiant unique du DTO.
     */
    private Long id;

    /**
     * Date et heure de création de l'entité.
     */
    private LocalDateTime createdDate;

    /**
     * Date et heure de la dernière modification de l'entité.
     */
    private LocalDateTime lastModifiedDate;

    /**
     * Nom de l'utilisateur qui a créé l'entité.
     */
    private String createdBy;

    /**
     * Nom de l'utilisateur ayant effectué la dernière modification.
     */
    private String lastModifiedBy;

    /**
     * Indique si l'entité est active ou non.
     */
    private boolean active = true;
}
