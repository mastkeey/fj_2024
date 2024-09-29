package ru.mastkey.fj_2024.lesson5.mapper;

import jakarta.annotation.Nonnull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoCategoryResponse;
import ru.mastkey.fj_2024.lesson5.configuration.MapperConfiguration;
import ru.mastkey.fj_2024.lesson5.entity.Category;

@Mapper(config = MapperConfiguration.class)
public interface KudaGoCategoryResponseToCategoryMapper extends Converter<KudaGoCategoryResponse, Category> {

    @Override
    Category convert(@Nonnull KudaGoCategoryResponse source);
}
