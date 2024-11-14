package ru.mastkey.fj_2024.lesson5.controller.event;

import org.junit.jupiter.api.Test;
import ru.mastkey.fj_2024.lesson5.controller.dto.EventRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.EventResponse;
import ru.mastkey.fj_2024.lesson5.exception.response.ErrorResponse;
import ru.mastkey.fj_2024.lesson5.support.IntegrationTestBase;

import static org.assertj.core.api.Assertions.assertThat;

class CreateEventTest extends IntegrationTestBase {
    private static final String PATH = "/events";

    @Test
    void createEventBadRequestTest() {
        var request = easyRandom.nextObject(EventRequest.class);
        request.setDate("1233-12-31");

        var errorResponse = webClient.post()
                .uri(PATH)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(errorResponse.getMessage()).isEqualTo(String.format("Place with id %s does not exist.", request.getPlaceId()));
    }

    @Test
    void createEventBadRequestWithNotValidDataTest() {
        var request = easyRandom.nextObject(EventRequest.class);
        var place = createPlace();
        request.setPlaceId(place.getId());

        var errorResponse = webClient.post()
                .uri(PATH)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(errorResponse.getMessage()).isEqualTo("Поле date: Date must be in the format yyyy-MM-dd");
    }

    @Test
    void createEventSuccessTest() {
        var request = easyRandom.nextObject(EventRequest.class);
        request.setDate("2024-10-12");
        var place = createPlace();
        request.setPlaceId(place.getId());

        assertThat(eventRepository.findAll()).hasSize(0);

        var response = webClient.post()
                .uri(PATH)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(eventRepository.findAll()).hasSize(1);
        assertThat(response.getDate()).isEqualTo(request.getDate());
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getPlace().getId()).isEqualTo(request.getPlaceId());
    }
}
