package ru.mastkey.fj_2024.lesson5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import ru.mastkey.fj_2024.lesson5.configuration.MapperConfiguration;
import ru.mastkey.fj_2024.lesson5.controller.dto.CategoryResponse;
import ru.mastkey.fj_2024.lesson5.entity.Category;

@Mapper(config = MapperConfiguration.class)
public interface CategoryToCategoryResponseMapper extends Converter<Category, CategoryResponse> {
    @Override
    CategoryResponse convert(Category source);
}
