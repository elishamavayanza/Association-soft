package com.org.testApi.dto.request;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

/**
 * DTO de requête pour la création/mise à jour d'un MemberRoleHistory.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRoleHistoryRequestDTO extends BaseEntityDTO {

    private Long memberId;
    private Long roleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
