package ru.mastkey.fj_2024.lesson5.controller.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import ru.mastkey.fj_2024.lesson5.client.ApiClient;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoCategoryResponse;
import ru.mastkey.fj_2024.lesson5.controller.dto.CategoryRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.CategoryResponse;
import ru.mastkey.fj_2024.lesson5.entity.Category;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;
import ru.mastkey.fj_2024.lesson5.mapper.CategoryRequestToCategoryMapper;
import ru.mastkey.fj_2024.lesson5.repository.EntityRepository;
import ru.mastkey.fj_2024.lesson5.service.CategoryService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryServiceUnitTest {
    @Mock
    private EntityRepository<UUID, Category> categoryRepository;

    @Mock
    private ApiClient<KudaGoCategoryResponse> client;

    @Mock
    private ConversionService conversionService;

    @Mock
    private CategoryRequestToCategoryMapper categoryRequestToCategoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private UUID categoryId;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryId = UUID.randomUUID();
        category = new Category();
        category.setId(categoryId);
    }


    @Test
    void getCategoryById_ShouldReturnCategory_WhenCategoryExists() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(conversionService.convert(any(Category.class), eq(CategoryResponse.class))).thenReturn(new CategoryResponse());

        CategoryResponse response = categoryService.getCategoryById(categoryId);

        assertNotNull(response);
        verify(categoryRepository, times(1)).findById(categoryId);
    }


    @Test
    void getCategoryById_ShouldThrowException_WhenCategoryNotFound() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            categoryService.getCategoryById(categoryId);
        });

        assertEquals("Category with id '" + categoryId + "' not found", exception.getMessage());
    }


    @Test
    void createCategory_ShouldReturnCreatedCategory() {
        CategoryRequest request = new CategoryRequest();
        when(conversionService.convert(any(CategoryRequest.class), eq(Category.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(conversionService.convert(any(Category.class), eq(CategoryResponse.class))).thenReturn(new CategoryResponse());

        CategoryResponse response = categoryService.createCategory(request);

        assertNotNull(response);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }


    @Test
    void updateCategory_ShouldThrowException_WhenCategoryNotFound() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            categoryService.updateCategory(categoryId, new CategoryRequest());
        });

        assertEquals("Category with id '" + categoryId + "' not found", exception.getMessage());
    }


    @Test
    void deleteCategoryById_ShouldDeleteCategory_WhenCategoryExists() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.deleteCategoryById(categoryId);

        verify(categoryRepository, times(1)).deleteById(categoryId);
    }


    @Test
    void deleteCategoryById_ShouldThrowException_WhenCategoryNotFound() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            categoryService.deleteCategoryById(categoryId);
        });

        assertEquals("Category with id '" + categoryId + "' not found", exception.getMessage());
    }
}
