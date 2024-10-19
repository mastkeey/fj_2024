package ru.mastkey.fj_2024.lesson5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;
import ru.mastkey.fj_2024.lesson5.configuration.MapperConfiguration;
import ru.mastkey.fj_2024.lesson5.controller.dto.EventRequest;
import ru.mastkey.fj_2024.lesson5.entity.Event;

@Mapper(config = MapperConfiguration.class)
public interface EventRequestToEventMapper extends Converter<EventRequest, Event> {
    @Override
    Event convert(EventRequest source);

    @Mapping(target = "place", ignore = true)
    Event toEventForUpdate(@MappingTarget Event target, EventRequest source);
}
