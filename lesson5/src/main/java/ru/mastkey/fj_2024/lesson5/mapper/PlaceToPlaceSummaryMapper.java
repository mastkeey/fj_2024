package ru.mastkey.fj_2024.lesson5.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import ru.mastkey.fj_2024.lesson5.configuration.MapperConfiguration;
import ru.mastkey.fj_2024.lesson5.controller.dto.PlaceSummary;
import ru.mastkey.fj_2024.lesson5.entity.Place;

@Mapper(config = MapperConfiguration.class)
public interface PlaceToPlaceSummaryMapper extends Converter<Place, PlaceSummary> {
    @Override
    PlaceSummary convert(Place source);
}
