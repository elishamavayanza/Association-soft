package com.org.testApi.dto.response;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO de réponse pour l'entité User.
 */
@Getter
@Setter
@SuperBuilder
public class UserResponseDTO extends BaseEntityDTO {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean enabled;
}
