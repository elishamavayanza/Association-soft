package com.org.testApi.dto.response;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO de réponse pour l'entité Member.
 */
@Getter
@Setter
public class MemberResponseDTO extends BaseEntityDTO {

    private Long userId;
    private Long associationId;
    private LocalDate joinDate;
    private LocalDate leaveDate;
    private String type;
    private boolean isAdmin;
    private String memberCode;
}