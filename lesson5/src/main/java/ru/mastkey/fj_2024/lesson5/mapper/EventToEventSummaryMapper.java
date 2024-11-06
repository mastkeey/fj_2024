package ru.mastkey.fj_2024.lesson5.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import ru.mastkey.fj_2024.lesson5.configuration.MapperConfiguration;
import ru.mastkey.fj_2024.lesson5.controller.dto.EventSummary;
import ru.mastkey.fj_2024.lesson5.entity.Event;

@Mapper(config = MapperConfiguration.class)
public interface EventToEventSummaryMapper extends Converter<Event, EventSummary> {
    @Override
    EventSummary convert(Event source);
}
