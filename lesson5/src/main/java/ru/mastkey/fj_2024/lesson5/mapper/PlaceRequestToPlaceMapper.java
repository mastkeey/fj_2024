package ru.mastkey.fj_2024.lesson5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;
import ru.mastkey.fj_2024.lesson5.configuration.MapperConfiguration;
import ru.mastkey.fj_2024.lesson5.controller.dto.PlaceRequest;
import ru.mastkey.fj_2024.lesson5.entity.Place;

@Mapper(config = MapperConfiguration.class, uses = PlaceToPlaceSummaryMapper.class)
public interface PlaceRequestToPlaceMapper extends Converter<PlaceRequest, Place> {
    @Override
    Place convert(PlaceRequest source);

    @Mapping(target = "events", ignore = true)
    Place toPlaceForUpdate(@MappingTarget Place target, PlaceRequest source);
}
