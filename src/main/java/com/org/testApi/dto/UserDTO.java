package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour l'entité User.
 */
@Getter
@Setter
public class UserDTO extends BaseEntityDTO {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean enabled;
}
