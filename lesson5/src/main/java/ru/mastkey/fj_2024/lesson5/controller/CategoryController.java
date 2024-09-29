package ru.mastkey.fj_2024.lesson5.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mastkey.fj_2024.lesson5.controller.dto.CategoryRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.CategoryResponse;
import ru.mastkey.fj_2024.lesson5.service.CategoryService;
import ru.mastkey.ripperstarter.annotation.ExecutionTime;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/places/categories")
@RequiredArgsConstructor
@ExecutionTime
public class CategoryController implements BaseController<CategoryRequest, CategoryResponse> {

    private final CategoryService categoryService;

    @Override
    @SneakyThrows
    public ResponseEntity<CategoryResponse> getById(UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Override
    public ResponseEntity<List<CategoryResponse>> getAll() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Override
    public ResponseEntity<CategoryResponse> create(CategoryRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @Override
    public ResponseEntity<CategoryResponse> update(UUID id, CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));

    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok().build();
    }
}
