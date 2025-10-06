package com.org.testApi.dto.request;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

/**
 * DTO de requête pour la création/mise à jour d'un Project.
 */
@Getter
@Setter
@SuperBuilder
public class ProjectRequestDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long managerId;
    private Long associationId;
}
