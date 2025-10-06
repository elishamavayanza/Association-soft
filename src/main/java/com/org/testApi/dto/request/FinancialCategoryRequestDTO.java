package com.org.testApi.dto.request;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO de requête pour la création/mise à jour d'une FinancialCategory.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialCategoryRequestDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private String type;
}
