package ru.mastkey.fj_2024.lesson5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoLocationResponse;
import ru.mastkey.fj_2024.lesson5.configuration.MapperConfiguration;
import ru.mastkey.fj_2024.lesson5.entity.Location;

@Mapper(config = MapperConfiguration.class)
public interface KudaGoLocationResponseToLocationMapper extends Converter<KudaGoLocationResponse, Location> {

    @Override
    Location convert(KudaGoLocationResponse source);
}
