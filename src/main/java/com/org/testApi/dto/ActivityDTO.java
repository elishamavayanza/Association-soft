package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité Activity.
 */
@Getter
@Setter
public class ActivityDTO extends BaseEntityDTO {

    private String title;
    private String description;
    private String type;
    private Long associationId;
    private Long projectId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
}
