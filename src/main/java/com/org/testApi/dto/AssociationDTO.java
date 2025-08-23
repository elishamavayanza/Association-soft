package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour l'entité Association.
 */
@Getter
@Setter
public class AssociationDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private String location; // Gardez le même nom que l'entité
    private String legalStatus;
    private String siret;
    private String email;
    private String phone;
    private Double membershipFeeAmount;
}