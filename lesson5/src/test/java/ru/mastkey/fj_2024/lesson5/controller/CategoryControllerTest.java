package ru.mastkey.fj_2024.lesson5.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.mastkey.fj_2024.lesson5.controller.dto.CategoryRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.CategoryResponse;
import ru.mastkey.fj_2024.lesson5.entity.Category;
import ru.mastkey.fj_2024.lesson5.suport.IntegrationTestBase;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryControllerTest extends IntegrationTestBase {

    private final String PATH = "/api/v1/places/categories";

    @Test
    void getCategoryByIdTest() {
        var category = easyRandom.nextObject(Category.class);
        categoryRepository.save(category);

        var expectedResponse = conversionService.convert(category, CategoryResponse.class);

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH + "/{id}")
                        .build(category.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryResponse.class)
                .returnResult();

        assertThat(response.getResponseBody()).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    void getCategoryByIdTestNotFound() {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH + "/{id}")
                        .build(UUID.randomUUID()))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllCategoriesTest() {

        var expectedSize = categoryRepository.findAll().size();

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .returnResult();

        assertThat(response.getResponseBody().size()).isEqualTo(expectedSize);
    }

    @Test
    void createCategoryTest() {
        var category = easyRandom.nextObject(CategoryRequest.class);

        var expectedSize = categoryRepository.findAll().size();

        var response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .build())
                .bodyValue(category)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryResponse.class)
                .returnResult();

        assertThat(categoryRepository.findAll().size()).isEqualTo(expectedSize + 1);
        assertThat(response.getResponseBody().getName()).isEqualTo(category.getName());
        assertThat(response.getResponseBody().getSlug()).isEqualTo(category.getSlug());
    }

    @Test
    void updateCategoryTest() {
        var category = easyRandom.nextObject(Category.class);
        categoryRepository.save(category);
        var requestForUpdate = new CategoryRequest();
        requestForUpdate.setName("update");
        requestForUpdate.setSlug("update");

        var response = webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH + "/{id}")
                        .build(category.getId()))
                .bodyValue(requestForUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryResponse.class)
                .returnResult();

        assertThat(response.getResponseBody().getName()).isEqualTo(requestForUpdate.getName());
        assertThat(response.getResponseBody().getSlug()).isEqualTo(requestForUpdate.getSlug());
    }

    @Test
    void deleteCategoryTest() {
        var category = easyRandom.nextObject(Category.class);
        categoryRepository.save(category);

        webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH + "/{id}")
                        .build(category.getId()))
                .exchange()
                .expectStatus().isOk();

        assertThat(categoryRepository.findById(category.getId())).isEmpty();
    }

    @AfterEach
    void cleanUp() {
        categoryRepository.deleteAll();
    }

}