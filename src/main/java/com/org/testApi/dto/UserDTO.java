package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * DTO pour l'entité User.
 */
@Getter
@Setter
@SuperBuilder
public class UserDTO extends BaseEntityDTO {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean enabled;
    private Set<RoleDTO> roles;
    private String password;
}
