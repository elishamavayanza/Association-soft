package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO pour l'entité Document.
 */
@Getter
@Setter
@SuperBuilder
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
