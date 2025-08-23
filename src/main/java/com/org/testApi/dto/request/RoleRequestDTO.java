package com.org.testApi.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO de requête pour la création/mise à jour d'un Role.
 */
@Getter
@Setter
public class RoleRequestDTO {

    private Integer id;
    private String name;
}
