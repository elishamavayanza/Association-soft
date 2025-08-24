package com.org.testApi.payload;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityPayload extends BasePayload {
    private Long id;
    private String title;
    private String description;
    private String type;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private Boolean deleted;
    private Long associationId;
    private Long projectId;
    private Long creatorId;
    private Long[] participantIds;
    private String status;

}
