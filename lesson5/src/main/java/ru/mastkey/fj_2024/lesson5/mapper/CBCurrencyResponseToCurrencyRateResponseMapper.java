package ru.mastkey.fj_2024.lesson5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import ru.mastkey.fj_2024.lesson5.client.dto.CBCurrencyResponse;
import ru.mastkey.fj_2024.lesson5.configuration.MapperConfiguration;
import ru.mastkey.fj_2024.lesson5.controller.dto.CurrencyRateResponse;

@Mapper(config = MapperConfiguration.class)
public interface CBCurrencyResponseToCurrencyRateResponseMapper extends Converter<CBCurrencyResponse, CurrencyRateResponse> {
    @Override
    @Mapping(target = "currency", source = "charCode")
    @Mapping(target = "rate", source = "vunitRate")
    CurrencyRateResponse convert(CBCurrencyResponse source);
}
