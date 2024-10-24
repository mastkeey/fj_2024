package ru.mastkey.fj_2024.lesson5.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import ru.mastkey.fj_2024.lesson5.configuration.MapperConfiguration;
import ru.mastkey.fj_2024.lesson5.controller.dto.PlaceResponse;
import ru.mastkey.fj_2024.lesson5.entity.Place;

@Mapper(config = MapperConfiguration.class, uses = EventToEventSummaryMapper.class)
public interface PlaceToPlaceResponseMapper extends Converter<Place, PlaceResponse> {
    @Override
    PlaceResponse convert(Place source);
}
