package com.org.testApi.dto.request;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO de requête pour la création/mise à jour d'un Document.
 */
@Getter
@Setter
public class DocumentRequestDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private String filePath;
    private String fileType;
    private Long activityId;
    private Long projectId;
    private Long associationId;
    private Long memberId;
}
