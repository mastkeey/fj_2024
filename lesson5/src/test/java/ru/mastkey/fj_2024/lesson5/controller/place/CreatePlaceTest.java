package ru.mastkey.fj_2024.lesson5.controller.place;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.mastkey.fj_2024.lesson5.controller.dto.PlaceRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.PlaceResponse;
import ru.mastkey.fj_2024.lesson5.entity.Place;
import ru.mastkey.fj_2024.lesson5.exception.response.ErrorResponse;
import ru.mastkey.fj_2024.lesson5.suport.IntegrationTestBase;

import static org.assertj.core.api.Assertions.assertThat;

class CreatePlaceTest extends IntegrationTestBase {
    private static final String PATH = "/places";

    @Test
    void createPlaceSuccessTest() {
        var request = createRequest();

        assertThat(placeRepository.findAll()).hasSize(0);

        var response = webClient.post()
                .uri(PATH)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlaceResponse.class)
                .returnResult().getResponseBody();

        assertThat(placeRepository.findAll()).hasSize(1);

        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getCity()).isEqualTo(request.getCity());
        assertThat(response.getAddress()).isEqualTo(request.getAddress());
        assertThat(response.getEvents()).isEmpty();
    }

    @Test
    void createPlaceConflictTest() {
        var request = createRequest();
        var place = new Place();
        place.setName(request.getName());
        place.setCity(request.getCity());
        place.setAddress(request.getAddress());
        placeRepository.save(place);

        var errorResponse = webClient.post()
                .uri(PATH)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(errorResponse.getMessage()).isEqualTo(String.format("Place with name %s, address %s, city %s already exist.", request.getName(), request.getAddress(), request.getCity()));
    }

    private PlaceRequest createRequest() {
        var request = new PlaceRequest();
        request.setName("testName");
        request.setCity("testCity");
        request.setAddress("testAddress");
        return request;
    }
}
