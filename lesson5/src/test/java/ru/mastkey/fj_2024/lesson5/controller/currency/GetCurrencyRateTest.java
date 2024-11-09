package ru.mastkey.fj_2024.lesson5.controller.currency;

import org.junit.jupiter.api.Test;
import ru.mastkey.fj_2024.lesson5.controller.dto.CurrencyRateResponse;
import ru.mastkey.fj_2024.lesson5.exception.response.ErrorResponse;
import ru.mastkey.fj_2024.lesson5.support.IntegrationTestBase;

import static org.assertj.core.api.Assertions.assertThat;

class GetCurrencyRateTest extends IntegrationTestBase {
    private static final String URL = "/currencies/rates/{charCode}";

    @Test
    void getCurrencyRateSuccessfulTest() {
        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URL)
                        .build("USD"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CurrencyRateResponse.class)
                .returnResult();

        var rate = response.getResponseBody();
        assertThat(rate.getCurrency()).isEqualTo("USD");
        assertThat(rate.getName()).isEqualTo("Доллар США");
        assertThat(rate.getRate()).isEqualTo("94,8700");
    }

    @Test
    void getCurrencyRateBadRequestTest() {
        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URL)
                        .build("test"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult();

        var error = response.getResponseBody();

        assertThat(error.getStatus()).isEqualTo(400);
        assertThat(error.getMessage()).isEqualTo("Non-existent currency code - test");
        assertThat(error.getCode()).isEqualTo("BadRequest");
    }

    @Test
    void getCurrencyRateNotFoundTest() {
        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URL)
                        .build("RUB"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult();

        var error = response.getResponseBody();

        assertThat(error.getStatus()).isEqualTo(404);
        assertThat(error.getMessage()).isEqualTo("Unsupported currency code - RUB");
        assertThat(error.getCode()).isEqualTo("NotFound");
    }
}
