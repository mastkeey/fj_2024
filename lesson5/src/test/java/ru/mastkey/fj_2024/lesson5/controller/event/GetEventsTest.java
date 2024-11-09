package ru.mastkey.fj_2024.lesson5.controller.event;

import org.junit.jupiter.api.Test;
import ru.mastkey.fj_2024.lesson5.controller.dto.EventResponse;
import ru.mastkey.fj_2024.lesson5.entity.Event;
import ru.mastkey.fj_2024.lesson5.support.IntegrationTestBase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class GetEventsTest extends IntegrationTestBase {
    private static final String PATH = "/events";

    @Test
    void getEventsSuccessTest() {
        IntStream.range(0, 5).forEach(i -> {
            createEvent();
        });

        var response = webClient.get()
                .uri(PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).hasSize(5);
    }

    @Test
    void getEventsWithEmptyFilterTest() {
        IntStream.range(0, 5).forEach(i -> {
            createEvent();
        });

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .queryParam("filtering", "")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).hasSize(5);
    }

    @Test
    void getEventsWithFilterByNameTest() {
        var events = new ArrayList<Event>();
        IntStream.range(0, 5).forEach(i -> {
           events.add(createEvent());
        });

        var event = events.get((int) (Math.random() * (events.size() - 1)));
        var filter = String.format("name eq %s", event.getName());

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .queryParam("filtering", filter)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).hasSize(1);
        assertThat(response.get(0).getName()).isEqualTo(event.getName());
    }

    @Test
    void getEventsWithFilterByPlaceNameTest() {
        var events = new ArrayList<Event>();
        IntStream.range(0, 5).forEach(i -> {
            events.add(createEvent());
        });

        var event = events.get((int) (Math.random() * (events.size() - 1)));
        var place = event.getPlace();
        var filter = String.format("place.name eq %s", place.getName());

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .queryParam("filtering", filter)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).hasSize(1);
        assertThat(response.get(0).getPlace().getName()).isEqualTo(place.getName());
    }

    @Test
    void getEventsWithFilterByNameZeroMatchesTest() {
        var events = new ArrayList<Event>();
        IntStream.range(0, 5).forEach(i -> {
            events.add(createEvent());
        });

        var filter = String.format("name eq %s", "test52");

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .queryParam("filtering", filter)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).hasSize(0);
    }

    @Test
    void getEventsWithFilterByDateLikeTest() {

        var event = createEvent();
        event.setDate(LocalDate.of(2024, 1, 12));
        eventRepository.save(event);

        var filter = String.format("date like %s", "2024-01");

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .queryParam("filtering", filter)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).hasSize(1);
    }

    @Test
    void getEventsWithFilterByDateEqualTest() {

        var event = createEvent();
        event.setDate(LocalDate.of(2024, 1, 12));
        eventRepository.save(event);

        var filter = String.format("date like %s", "2024-01-12");

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .queryParam("filtering", filter)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).hasSize(1);
    }

    @Test
    void getEventsWithFilterByDateGreaterOrEqualFindTest() {

        var event = createEvent();
        event.setDate(LocalDate.of(2024, 1, 12));
        eventRepository.save(event);

        var filter = String.format("date ge %s", "2020-01-12");

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .queryParam("filtering", filter)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).hasSize(1);
    }

    @Test
    void getEventsWithFilterByDateGreaterOrEqualNotFoundTest() {

        var event = createEvent();
        event.setDate(LocalDate.of(2024, 1, 12));
        eventRepository.save(event);

        var filter = String.format("date ge %s", "2025-01-12");

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .queryParam("filtering", filter)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).hasSize(0);
    }

    @Test
    void getEventsWithFilterByDateLessOrEqualNotFoundTest() {

        var event = createEvent();
        event.setDate(LocalDate.of(2024, 1, 12));
        eventRepository.save(event);

        var filter = String.format("date le %s", "2023-01-12");

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .queryParam("filtering", filter)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).hasSize(0);
    }

    @Test
    void getEventsWithFilterByDateLessOrEqualFoundTest() {

        var event = createEvent();
        event.setDate(LocalDate.of(2024, 1, 12));
        eventRepository.save(event);

        var filter = String.format("date le %s", "2025-01-12");

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .queryParam("filtering", filter)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).hasSize(1);
    }

    @Test
    void getEventsWithFilterUnknownOperatorTest() {

        IntStream.range(0, 5).forEach(i -> {
            createEvent();
        });

        var filter = String.format("date test %s", "2025-01-12");

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .queryParam("filtering", filter)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).hasSize(5);
    }

    @Test
    void getEventsWithFilterByPlaceNameAndDateFindTest() {
        var events = new ArrayList<Event>();
        IntStream.range(0, 5).forEach(i -> {
            events.add(createEvent());
        });

        var event = events.get(0);
        event.setDate(LocalDate.of(2222, 2, 22));
        event.setName("222");
        eventRepository.save(event);

        var filter = String.format("name eq %s, date eq %s", event.getName(), event.getDate());

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .queryParam("filtering", filter)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).hasSize(1);
    }

    @Test
    void getEventsWithFilterByPlaceNameAndDateNotFindTest() {
        var events = new ArrayList<Event>();
        IntStream.range(0, 5).forEach(i -> {
            events.add(createEvent());
        });

        var event = events.get(0);
        event.setDate(LocalDate.of(2222, 2, 22));
        event.setName("222");
        eventRepository.save(event);

        var filter = String.format("name eq %s, date eq %s", "test", event.getDate());

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .queryParam("filtering", filter)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).hasSize(0);
    }
}
