package com.org.testApi.dto.response;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de réponse pour l'entité Association.
 */
@Getter
@Setter
public class AssociationResponseDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private String address; // Pour la réponse, on peut garder "address"
    private String email;
    private String phone;
    private Double membershipFeeAmount;
}