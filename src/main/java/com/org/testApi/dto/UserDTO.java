package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

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
    private Set<RoleDTO> roles;
}
