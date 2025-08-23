package com.org.testApi.dto.response;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * DTO de réponse pour l'entité Project.
 */
@Getter
@Setter
public class ProjectResponseDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long managerId;
    private Long associationId;
}
