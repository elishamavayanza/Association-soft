package com.org.testApi.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AssociationPayload extends BasePayload {
    private Long id;
    private String name;
    private String description;
    private String location;
    private String legalStatus;
    private String siret;
    private List<Long> memberIds;
    private List<Long> activityIds;
    private List<Long> transactionIds;
    private List<Long> documentIds;
}
