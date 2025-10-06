package com.org.testApi.dto.request;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO de requête pour la création/mise à jour d'un User.
 */
@Getter
@Setter
@SuperBuilder
public class UserRequestDTO extends BaseEntityDTO {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean enabled;
}
