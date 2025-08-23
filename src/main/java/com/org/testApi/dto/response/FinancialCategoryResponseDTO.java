package com.org.testApi.dto.response;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de réponse pour l'entité FinancialCategory.
 */
@Getter
@Setter
public class FinancialCategoryResponseDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private String type;
}
