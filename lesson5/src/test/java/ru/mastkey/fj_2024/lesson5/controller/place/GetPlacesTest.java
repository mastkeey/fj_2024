package ru.mastkey.fj_2024.lesson5.controller.place;

import org.junit.jupiter.api.Test;
import ru.mastkey.fj_2024.lesson5.controller.dto.PlaceResponse;
import ru.mastkey.fj_2024.lesson5.entity.Place;
import ru.mastkey.fj_2024.lesson5.suport.IntegrationTestBase;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class GetPlacesTest extends IntegrationTestBase {
    private static final String PATH = "/places";

    @Test
    void getPlacesSuccessTest() {

        IntStream.range(0, 5).forEach(i -> {
            var place = easyRandom.nextObject(Place.class);
            place.setEvents(null);
            placeRepository.save(place);
        });

        var response = webClient.get()
                .uri(PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PlaceResponse.class)
                .returnResult().getResponseBody();

        assertThat(response.size()).isEqualTo(5);
    }
}
