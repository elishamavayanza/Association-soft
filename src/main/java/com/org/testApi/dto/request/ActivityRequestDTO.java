package com.org.testApi.dto.request;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO de requête pour la création/mise à jour d'une Activity.
 */
@Getter
@Setter
public class ActivityRequestDTO extends BaseEntityDTO {

    private String title;
    private String description;
    private String type;
    private Long associationId;
    private Long projectId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
}
