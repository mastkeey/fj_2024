package ru.mastkey.fj_2024.lesson5.controller.event;

import org.junit.jupiter.api.Test;
import ru.mastkey.fj_2024.lesson5.exception.response.ErrorResponse;
import ru.mastkey.fj_2024.lesson5.suport.IntegrationTestBase;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteEventByIdTest extends IntegrationTestBase {
    private static final String PATH = "/events/{id}";

    @Test
    void deleteEventByIdNotFoundTest() {
        var id = UUID.randomUUID();

        var errorResponse = webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .build(id))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(errorResponse.getMessage()).isEqualTo("Event with id %s not found.", id);
    }

    @Test
    void deleteEventByIdSuccessTest() {
        var event = createEvent();

        assertThat(eventRepository.findAll()).hasSize(1);

        webClient.delete()
                .uri(PATH, event.getId())
                .exchange()
                .expectStatus().isOk();

        assertThat(eventRepository.findAll()).hasSize(0);
    }
}
