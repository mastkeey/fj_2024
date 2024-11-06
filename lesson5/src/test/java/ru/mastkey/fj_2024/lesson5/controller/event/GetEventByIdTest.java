package ru.mastkey.fj_2024.lesson5.controller.event;

import org.junit.jupiter.api.Test;
import ru.mastkey.fj_2024.lesson5.controller.dto.EventResponse;
import ru.mastkey.fj_2024.lesson5.exception.response.ErrorResponse;
import ru.mastkey.fj_2024.lesson5.suport.IntegrationTestBase;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GetEventByIdTest extends IntegrationTestBase {
    private static final String PATH = "/events/{id}";

    @Test
    void getEventByIdNotFoundTest() {
        var id = UUID.randomUUID();

        var errorResponse = webClient.get()
                .uri(PATH, id)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(errorResponse.getMessage()).isEqualTo(String.format("Event with id %s not found.",id));
    }

    @Test
    void getEventByIdSuccessTest() {
        var savedEvent = createEvent();
        var savedPlace = savedEvent.getPlace();

        var response = webClient.get()
                .uri(PATH, savedEvent.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response.getName()).isEqualTo(savedEvent.getName());
        assertThat(response.getDate()).isEqualTo(savedEvent.getDate().toString());

        var responsePlace = response.getPlace();

        assertThat(responsePlace.getName()).isEqualTo(savedPlace.getName());
        assertThat(responsePlace.getId()).isEqualTo(savedPlace.getId());
        assertThat(responsePlace.getAddress()).isEqualTo(savedPlace.getAddress());
        assertThat(responsePlace.getCity()).isEqualTo(savedPlace.getCity());
    }
}
