package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

/**
 * DTO pour l'entité Project.
 */
@Getter
@Setter
@SuperBuilder
public class ProjectDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long managerId;
    private Long associationId;
}
