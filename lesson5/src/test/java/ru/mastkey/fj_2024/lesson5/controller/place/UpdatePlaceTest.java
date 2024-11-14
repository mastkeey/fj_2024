package ru.mastkey.fj_2024.lesson5.controller.place;

import org.junit.jupiter.api.Test;
import ru.mastkey.fj_2024.lesson5.controller.dto.PlaceRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.PlaceResponse;
import ru.mastkey.fj_2024.lesson5.exception.response.ErrorResponse;
import ru.mastkey.fj_2024.lesson5.support.IntegrationTestBase;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UpdatePlaceTest extends IntegrationTestBase {
    private static final String PATH = "/places/{id}";

    @Test
    void updatePlaceByIdNotFoundTest() {
        var id = UUID.randomUUID();
        var request = easyRandom.nextObject(PlaceRequest.class);

        var errorResponse = webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .build(id))
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(errorResponse.getMessage()).isEqualTo("Place with id %s not found.", id);
    }

    @Test
    void updatePlaceByIdSuccessTest() {
        var savedPlace = createPlaceWithEvent();
        var savedEvent = savedPlace.getEvents().get(0);

        var request = easyRandom.nextObject(PlaceRequest.class);

        var response = webClient.put()
                .uri(PATH, savedPlace.getId())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlaceResponse.class)
                .returnResult().getResponseBody();

        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getAddress()).isEqualTo(request.getAddress());
        assertThat(response.getCity()).isEqualTo(request.getCity());
        assertThat(response.getId()).isEqualTo(savedPlace.getId());

        var responseEvent = response.getEvents().get(0);
        assertThat(responseEvent.getName()).isEqualTo(savedEvent.getName());
        assertThat(responseEvent.getDate()).isEqualTo(savedEvent.getDate().toString());
        assertThat(responseEvent.getId()).isEqualTo(savedEvent.getId());

    }
}
