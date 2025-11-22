package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

/**
 * DTO pour l'entité MemberRoleHistory.
 */
@Getter
@Setter
@SuperBuilder
public class MemberRoleHistoryDTO extends BaseEntityDTO {

    private Long memberId;
    private Long roleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
