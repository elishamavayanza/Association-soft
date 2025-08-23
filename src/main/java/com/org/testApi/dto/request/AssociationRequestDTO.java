package com.org.testApi.dto.request;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de requête pour la création/mise à jour d'une Association.
 */
@Getter
@Setter
public class AssociationRequestDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private String address; // Pour la requête, on peut garder "address"
    private String email;
    private String phone;
    private Double membershipFeeAmount;
}