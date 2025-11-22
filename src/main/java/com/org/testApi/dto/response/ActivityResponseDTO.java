package com.org.testApi.dto.response;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

/**
 * DTO de réponse pour l'entité Activity.
 */
@Getter
@Setter
@SuperBuilder
public class ActivityResponseDTO extends BaseEntityDTO {

    private String title;
    private String description;
    private String type;
    private Long associationId;
    private Long projectId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
}
