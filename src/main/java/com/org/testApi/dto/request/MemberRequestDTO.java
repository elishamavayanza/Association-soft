package com.org.testApi.dto.request;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO de requête pour la création/mise à jour d'un Member.
 */
@Getter
@Setter
public class MemberRequestDTO extends BaseEntityDTO {

    private Long userId;
    private Long associationId;
    private LocalDate joinDate;
    private LocalDate leaveDate;
    private String type;
    private boolean isAdmin;
}
