package ru.mastkey.fj_2024.lesson5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;
import ru.mastkey.fj_2024.lesson5.configuration.MapperConfiguration;
import ru.mastkey.fj_2024.lesson5.controller.dto.LocationRequest;
import ru.mastkey.fj_2024.lesson5.entity.Location;

@Mapper(config = MapperConfiguration.class)
public interface LocationRequestToLocationMapper extends Converter<LocationRequest, Location> {
    @Override
    Location convert(LocationRequest source);

    @Mapping(target = "id", ignore = true)
    Location toLocationForUpdate(@MappingTarget Location target, LocationRequest source);
}
