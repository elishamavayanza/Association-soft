package com.org.testApi.dto.request;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de requête pour la création/mise à jour d'une FinancialCategory.
 */
@Getter
@Setter
public class FinancialCategoryRequestDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private String type;
}
