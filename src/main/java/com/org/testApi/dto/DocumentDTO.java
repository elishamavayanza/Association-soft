package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour l'entité Document.
 */
@Getter
@Setter
public class DocumentDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private String filePath;
    private String fileType;
    private Long activityId;
    private Long projectId;
    private Long associationId;
    private Long memberId;
}
