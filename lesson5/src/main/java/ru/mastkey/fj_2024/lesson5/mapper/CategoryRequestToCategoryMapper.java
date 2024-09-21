package ru.mastkey.fj_2024.lesson5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;
import ru.mastkey.fj_2024.lesson5.configuration.MapperConfiguration;
import ru.mastkey.fj_2024.lesson5.controller.dto.CategoryRequest;
import ru.mastkey.fj_2024.lesson5.entity.Category;

@Mapper(config = MapperConfiguration.class)
public interface CategoryRequestToCategoryMapper extends Converter<CategoryRequest, Category> {
    @Override
    Category convert(CategoryRequest source);

    @Mapping(target = "id", ignore = true)
    Category toCategoryForUpdate(@MappingTarget Category target, CategoryRequest source);
}
