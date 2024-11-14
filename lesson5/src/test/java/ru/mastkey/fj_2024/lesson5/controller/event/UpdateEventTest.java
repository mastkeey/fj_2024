package ru.mastkey.fj_2024.lesson5.controller.event;

import org.junit.jupiter.api.Test;
import ru.mastkey.fj_2024.lesson5.controller.dto.EventRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.EventResponse;
import ru.mastkey.fj_2024.lesson5.exception.response.ErrorResponse;
import ru.mastkey.fj_2024.lesson5.support.IntegrationTestBase;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateEventTest extends IntegrationTestBase {
    private static final String PATH = "/events/{id}";

    @Test
    void updateEventNotFoundTest() {
        var request = easyRandom.nextObject(EventRequest.class);
        request.setDate("1123-12-12");
        var id = UUID.randomUUID();

        var errorResponse = webClient.put()
                .uri(PATH, id)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(errorResponse.getMessage()).isEqualTo(String.format("Event with id %s not found.", id));
    }

    @Test
    void updateEventBadRequestTest() {
        var request = easyRandom.nextObject(EventRequest.class);
        request.setDate("1123-12-12");
        var savedEvent = createEvent();

        var errorResponse = webClient.put()
                .uri(PATH, savedEvent.getId())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(errorResponse.getMessage()).isEqualTo(String.format("Place with id %s does not exist.", request.getPlaceId()));
    }

    @Test
    void updateEventSuccessTest() {
        var request = easyRandom.nextObject(EventRequest.class);
        request.setDate("1123-12-12");
        var eventForUpdate = createEvent();
        var newPlace = createPlace();
        request.setPlaceId(newPlace.getId());

        var response = webClient.put()
                .uri(PATH, eventForUpdate.getId())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response.getDate()).isEqualTo(request.getDate());
        assertThat(response.getPlace().getId()).isEqualTo(newPlace.getId());
        assertThat(response.getName()).isEqualTo(request.getName());
    }
}
