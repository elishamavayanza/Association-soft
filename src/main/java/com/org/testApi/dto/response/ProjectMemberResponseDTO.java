package com.org.testApi.dto.response;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * DTO de réponse pour l'entité ProjectMember.
 */
@Getter
@Setter
public class ProjectMemberResponseDTO extends BaseEntityDTO {

    private Long projectId;
    private Long memberId;
    private LocalDate joinDate;
    private LocalDate leaveDate;
    private String role;
}
