package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * DTO pour l'entité ProjectMember.
 */
@Getter
@Setter
public class ProjectMemberDTO extends BaseEntityDTO {

    private Long projectId;
    private Long memberId;
    private LocalDate joinDate;
    private LocalDate leaveDate;
    private String role;
}
