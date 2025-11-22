package com.org.testApi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO pour l'entité FinancialCategory.
 */
@Getter
@Setter
@SuperBuilder
public class FinancialCategoryDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private String type; // INCOME ou EXPENSE
}
