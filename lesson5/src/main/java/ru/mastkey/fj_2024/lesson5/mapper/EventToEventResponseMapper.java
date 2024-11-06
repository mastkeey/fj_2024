package ru.mastkey.fj_2024.lesson5.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import ru.mastkey.fj_2024.lesson5.configuration.MapperConfiguration;
import ru.mastkey.fj_2024.lesson5.controller.dto.EventResponse;
import ru.mastkey.fj_2024.lesson5.entity.Event;

@Mapper(config = MapperConfiguration.class, uses = PlaceToPlaceSummaryMapper.class)
public interface EventToEventResponseMapper extends Converter<Event, EventResponse> {
    @Override
    EventResponse convert(Event source);
}
