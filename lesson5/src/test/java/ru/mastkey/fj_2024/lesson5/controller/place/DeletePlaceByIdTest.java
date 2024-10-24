package ru.mastkey.fj_2024.lesson5.controller.place;

import org.junit.jupiter.api.Test;
import ru.mastkey.fj_2024.lesson5.entity.Place;
import ru.mastkey.fj_2024.lesson5.exception.response.ErrorResponse;
import ru.mastkey.fj_2024.lesson5.suport.IntegrationTestBase;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DeletePlaceByIdTest extends IntegrationTestBase {
    private static final String PATH = "/places/{id}";

    @Test
    void deletePlaceByIdNotFoundTest() {
        var id = UUID.randomUUID();

        var errorResponse = webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .build(id))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(errorResponse.getMessage()).isEqualTo("Place with id %s not found.", id);
    }

    @Test
    void deletePlaceByIdSuccessTest() {
        var place = createPlaceWithEvent();

        assertThat(placeRepository.findAll()).hasSize(1);
        assertThat(eventRepository.findAll()).hasSize(1);

        webClient.delete()
                .uri(PATH, place.getId())
                .exchange()
                .expectStatus().isOk();

        assertThat(eventRepository.findAll()).hasSize(0);
        assertThat(placeRepository.findAll()).hasSize(0);
    }
}
