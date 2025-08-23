package com.org.testApi.dto.response;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * DTO de réponse pour l'entité MemberRoleHistory.
 */
@Getter
@Setter
public class MemberRoleHistoryResponseDTO extends BaseEntityDTO {

    private Long memberId;
    private Long roleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
