package com.org.testApi.mapper;

import com.org.testApi.dto.ExchangeRateDTO;
import com.org.testApi.models.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExchangeRateMapper extends BaseMapper<ExchangeRate, ExchangeRateDTO> {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    @Override
    @Mapping(target = "fromCurrency", source = "fromCurrency")
    @Mapping(target = "toCurrency", source = "toCurrency")
    @Mapping(target = "rate", source = "rate")
    @Mapping(target = "lastUpdated", source = "lastUpdated")
    ExchangeRateDTO toDto(ExchangeRate entity);

    @Override
    @Mapping(target = "fromCurrency", source = "fromCurrency")
    @Mapping(target = "toCurrency", source = "toCurrency")
    @Mapping(target = "rate", source = "rate")
    @Mapping(target = "lastUpdated", source = "lastUpdated")
    ExchangeRate toEntity(ExchangeRateDTO dto);
}