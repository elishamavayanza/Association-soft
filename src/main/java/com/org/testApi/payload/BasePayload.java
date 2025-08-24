package com.org.testApi.payload;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BasePayload {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
}
