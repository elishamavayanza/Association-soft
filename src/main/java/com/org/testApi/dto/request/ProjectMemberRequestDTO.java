package com.org.testApi.dto.request;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * DTO de requête pour la création/mise à jour d'un ProjectMember.
 */
@Getter
@Setter
public class ProjectMemberRequestDTO extends BaseEntityDTO {

    private Long projectId;
    private Long memberId;
    private LocalDate joinDate;
    private LocalDate leaveDate;
    private String role;
}
