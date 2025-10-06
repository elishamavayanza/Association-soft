package com.org.testApi.dto.response;

import com.org.testApi.dto.BaseEntityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO de réponse pour l'entité FinancialCategory.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialCategoryResponseDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private String type;
}
