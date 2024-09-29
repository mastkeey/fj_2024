package ru.mastkey.fj_2024.lesson5.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.mastkey.fj_2024.lesson5.controller.dto.LocationRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.LocationResponse;
import ru.mastkey.fj_2024.lesson5.entity.Location;
import ru.mastkey.fj_2024.lesson5.suport.IntegrationTestBase;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LocationControllerTest extends IntegrationTestBase {
    private final String PATH = "/api/v1/places/locations";

    @Test
    void getLocationByIdTest() {
        var location = easyRandom.nextObject(Location.class);
        locationRepository.save(location);

        var expectedResponse = conversionService.convert(location, LocationResponse.class);

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH + "/{id}")
                        .build(location.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(LocationResponse.class)
                .returnResult();

        assertThat(response.getResponseBody()).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    void getLocationByIdTestNotFound() {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH + "/{id}")
                        .build(UUID.randomUUID()))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllLocationTest() {

        var expectedSize = locationRepository.findAll().size();

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
    void createLocationTest() {
        var location = easyRandom.nextObject(LocationRequest.class);

        var expectedSize = locationRepository.findAll().size();

        var response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .build())
                .bodyValue(location)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LocationResponse.class)
                .returnResult();

        assertThat(locationRepository.findAll().size()).isEqualTo(expectedSize + 1);
        assertThat(response.getResponseBody().getName()).isEqualTo(location.getName());
        assertThat(response.getResponseBody().getSlug()).isEqualTo(location.getSlug());
    }

    @Test
    void updateLocationTest() {
        var location = easyRandom.nextObject(Location.class);
        locationRepository.save(location);
        var requestForUpdate = new LocationRequest();
        requestForUpdate.setName("update");
        requestForUpdate.setSlug("update");

        var response = webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH + "/{id}")
                        .build(location.getId()))
                .bodyValue(requestForUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LocationResponse.class)
                .returnResult();

        assertThat(response.getResponseBody().getName()).isEqualTo(requestForUpdate.getName());
        assertThat(response.getResponseBody().getSlug()).isEqualTo(requestForUpdate.getSlug());
    }

    @Test
    void deleteCategoryTest() {
        var location = easyRandom.nextObject(Location.class);
        locationRepository.save(location);

        webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH + "/{id}")
                        .build(location.getId()))
                .exchange()
                .expectStatus().isOk();

        assertThat(locationRepository.findById(location.getId())).isEmpty();
    }

    @AfterEach
    void cleanUp() {
        locationRepository.deleteAll();
    }
}