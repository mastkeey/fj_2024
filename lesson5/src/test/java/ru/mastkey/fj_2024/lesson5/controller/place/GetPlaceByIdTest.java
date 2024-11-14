package ru.mastkey.fj_2024.lesson5.controller.place;

import org.junit.jupiter.api.Test;
import ru.mastkey.fj_2024.lesson5.controller.dto.PlaceResponse;
import ru.mastkey.fj_2024.lesson5.exception.response.ErrorResponse;
import ru.mastkey.fj_2024.lesson5.support.IntegrationTestBase;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GetPlaceByIdTest extends IntegrationTestBase {
    private static final String PATH = "/places/{id}";

    @Test
    void getPlaceByIdNotFoundTest() {
        var id = UUID.randomUUID();

        var errorResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .build(id))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(errorResponse.getMessage()).isEqualTo(String.format("Place with id %s not found.", id));
    }

    @Test
    void getPlaceByIdSuccessTest() {
        var place = createPlaceWithEvent();
        var event = place.getEvents().get(0);

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .build(place.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlaceResponse.class)
                .returnResult().getResponseBody();

        assertThat(response.getName()).isEqualTo(place.getName());
        assertThat(response.getAddress()).isEqualTo(place.getAddress());
        assertThat(response.getCity()).isEqualTo(place.getCity());
        assertThat(response.getId()).isEqualTo(place.getId());

        var responseEvent = response.getEvents().get(0);
        assertThat(responseEvent.getName()).isEqualTo(event.getName());
        assertThat(responseEvent.getDate()).isEqualTo(event.getDate().toString());
        assertThat(responseEvent.getId()).isEqualTo(event.getId());
    }
}
