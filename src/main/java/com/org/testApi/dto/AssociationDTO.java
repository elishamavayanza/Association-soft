package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour l'entité Association.
 * Utilisé comme DTO générique pour mapper les données principales de l'association.
 */
@Getter
@Setter
public class AssociationDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private String location;
    private String legalStatus;
    private String siret;
    private String email; // optionnel si tu veux garder contact
    private String phone; // idem
    private Double membershipFeeAmount; // correspond à membershipFeeAmount dans Request/Response DTO
}
