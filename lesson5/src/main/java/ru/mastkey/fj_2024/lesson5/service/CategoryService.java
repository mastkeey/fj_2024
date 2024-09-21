package ru.mastkey.fj_2024.lesson5.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.mastkey.fj_2024.lesson5.client.ApiClient;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoCategoryResponse;
import ru.mastkey.fj_2024.lesson5.controller.dto.CategoryRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.CategoryResponse;
import ru.mastkey.fj_2024.lesson5.entity.Category;
import ru.mastkey.fj_2024.lesson5.exception.ErrorType;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;
import ru.mastkey.fj_2024.lesson5.mapper.CategoryRequestToCategoryMapper;
import ru.mastkey.fj_2024.lesson5.repository.EntityRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private static final String MSG_CATEGORY_NOT_FOUND = "Category with id '%s' not found";
    private final EntityRepository<UUID, Category> categoryRepository;
    private final ApiClient<KudaGoCategoryResponse> client;
    private final ConversionService conversionService;
    private final CategoryRequestToCategoryMapper categoryRequestToCategoryMapper;

    @PostConstruct
    public void init() {
        var kudaGoCategoryResponses = client.getAllEntitiesFromKudaGo();
        var categories = kudaGoCategoryResponses.stream()
                .map(res -> conversionService.convert(res, Category.class))
                .toList();
        categoryRepository.saveAll(categories);
        System.out.println(categories.size());
    }

    public CategoryResponse getCategoryById(UUID uuid) {
        var category = findCategoryWithValidation(uuid);
        return conversionService.convert(category, CategoryResponse.class);
    }

    public List<CategoryResponse> getAllCategories() {
        var categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> conversionService.convert(category, CategoryResponse.class))
                .toList();
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        var category = conversionService.convert(request, Category.class);

        return conversionService.convert(categoryRepository.save(category), CategoryResponse.class);
    }

    public void deleteCategoryById(UUID id) {
        categoryRepository.deleteById(id);
    }

    public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
        var category = findCategoryWithValidation(id);
        var updatedCategory = categoryRequestToCategoryMapper.toCategoryForUpdate(category, request);

        return conversionService.convert(categoryRepository.save(updatedCategory), CategoryResponse.class);
    }

    private Category findCategoryWithValidation(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorType.NOT_FOUND, MSG_CATEGORY_NOT_FOUND, id));
    }
}
