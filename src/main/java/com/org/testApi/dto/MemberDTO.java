package com.org.testApi.dto;

import com.org.testApi.models.MemberType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * DTO pour l'entité Member.
 */
@Getter
@Setter
@SuperBuilder
public class MemberDTO extends BaseEntityDTO {

    private Long userId;
    private Long associationId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String photo;
    private LocalDate joinDate;
    private LocalDate leaveDate;
    private MemberType type;
    private boolean isAdmin;
    private String memberCode;
}