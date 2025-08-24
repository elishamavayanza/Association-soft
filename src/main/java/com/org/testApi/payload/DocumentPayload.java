package com.org.testApi.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentPayload extends BasePayload {
    private Long id;
    private String name;
    private String fileType;
    private String filePath;
    private LocalDateTime uploadDate;
    private Long fileSize;
    private Long associationId;
    private Long uploadedById;
}
