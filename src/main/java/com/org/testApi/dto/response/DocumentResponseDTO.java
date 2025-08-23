package com.org.testApi.dto.response;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO de réponse pour l'entité Document.
 */
@Getter
@Setter
public class DocumentResponseDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private String filePath;
    private String fileType;
    private Long activityId;
    private Long projectId;
    private Long associationId;
    private Long memberId;
}
