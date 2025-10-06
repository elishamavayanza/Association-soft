package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO pour l'entité Member.
 */
@Getter
@Setter
@SuperBuilder
public class MemberDTO extends BaseEntityDTO {

    private Long userId;
    private Long associationId;
    private LocalDate joinDate;
    private LocalDate leaveDate;
    private String type;
    private boolean isAdmin;
    private String memberCode;
}