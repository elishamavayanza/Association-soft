package com.org.testApi.dto.request;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * DTO de requête pour la création/mise à jour d'un MemberRoleHistory.
 */
@Getter
@Setter
public class MemberRoleHistoryRequestDTO extends BaseEntityDTO {

    private Long memberId;
    private Long roleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
