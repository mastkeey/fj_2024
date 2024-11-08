package ru.mastkey.fj_2024.lesson5.controller.currency;

import org.junit.jupiter.api.Test;
import ru.mastkey.fj_2024.lesson5.controller.dto.ConvertCurrencyRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.ConvertCurrencyResponse;
import ru.mastkey.fj_2024.lesson5.exception.response.ErrorResponse;
import ru.mastkey.fj_2024.lesson5.support.IntegrationTestBase;

import static org.assertj.core.api.Assertions.assertThat;

class PostConvertCurrencyTest extends IntegrationTestBase {

    private static final String URL = "/currencies/convert";

    @Test
    void convertCurrencyFromUsdToEurSuccessTest() {
        var request = createConvertCurrencyRequest("USD", "EUR", 1d);

        var response = webClient.post()
                .uri(URL)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ConvertCurrencyResponse.class)
                .returnResult();

        var convertCurrencyResponse = response.getResponseBody();

        assertThat(convertCurrencyResponse.getFromCurrency()).isEqualTo("USD");
        assertThat(convertCurrencyResponse.getToCurrency()).isEqualTo("EUR");
        assertThat(convertCurrencyResponse.getConvertedAmount()).isEqualTo("0.91");
    }

    @Test
    void convertCurrencyFromEurToUsdSuccessTest() {
        var request = createConvertCurrencyRequest("EUR", "USD", 1d);

        var response = webClient.post()
                .uri(URL)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ConvertCurrencyResponse.class)
                .returnResult();

        var convertCurrencyResponse = response.getResponseBody();

        assertThat(convertCurrencyResponse.getFromCurrency()).isEqualTo("EUR");
        assertThat(convertCurrencyResponse.getToCurrency()).isEqualTo("USD");
        assertThat(convertCurrencyResponse.getConvertedAmount()).isEqualTo("1.10");
    }

    @Test
    void convertCurrencyFromRubToUsdSuccessTest() {
        var request = createConvertCurrencyRequest("RUB", "USD", 1d);

        var response = webClient.post()
                .uri(URL)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ConvertCurrencyResponse.class)
                .returnResult();

        var convertCurrencyResponse = response.getResponseBody();

        assertThat(convertCurrencyResponse.getFromCurrency()).isEqualTo("RUB");
        assertThat(convertCurrencyResponse.getToCurrency()).isEqualTo("USD");
        assertThat(convertCurrencyResponse.getConvertedAmount()).isEqualTo("0.01");
    }

    @Test
    void convertCurrencyFromUsdToRubSuccessTest() {
        var request = createConvertCurrencyRequest("USD", "RUB", 1d);

        var response = webClient.post()
                .uri(URL)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ConvertCurrencyResponse.class)
                .returnResult();

        var convertCurrencyResponse = response.getResponseBody();

        assertThat(convertCurrencyResponse.getFromCurrency()).isEqualTo("USD");
        assertThat(convertCurrencyResponse.getToCurrency()).isEqualTo("RUB");
        assertThat(convertCurrencyResponse.getConvertedAmount()).isEqualTo("94.87");
    }

    @Test
    void convertCurrencyFromUsdToUsdSuccessTest() {
        var request = createConvertCurrencyRequest("USD", "USD", 1d);

        var response = webClient.post()
                .uri(URL)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ConvertCurrencyResponse.class)
                .returnResult();

        var convertCurrencyResponse = response.getResponseBody();

        assertThat(convertCurrencyResponse.getFromCurrency()).isEqualTo("USD");
        assertThat(convertCurrencyResponse.getToCurrency()).isEqualTo("USD");
        assertThat(convertCurrencyResponse.getConvertedAmount()).isEqualTo("1.00");
    }

    @Test
    void convertCurrencyNotFoundFromCurrencyTest() {
        var request = createConvertCurrencyRequest("THB", "RUB", 1d);
        var error = webClient.post()
                .uri(URL)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(error.getMessage()).isEqualTo("Unsupported currency code - THB");
    }

    @Test
    void convertCurrencyNotFoundToCurrencyTest() {
        var request = createConvertCurrencyRequest("RUB", "THB", 1d);
        var error = webClient.post()
                .uri(URL)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(error.getMessage()).isEqualTo("Unsupported currency code - THB");
    }

    @Test
    void convertCurrencyBadRequestNonExistFromCurrencyTest() {
        var request = createConvertCurrencyRequest("test", "RUB", 1d);
        var error = webClient.post()
                .uri(URL)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(error.getMessage()).isEqualTo("Non-existent currency code - test");
    }

    @Test
    void convertCurrencyBadRequestNonExistToCurrencyTest() {
        var request = createConvertCurrencyRequest("RUB", "test", 1d);
        var error = webClient.post()
                .uri(URL)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(error.getMessage()).isEqualTo("Non-existent currency code - test");
    }

    @Test
    void convertCurrencyBadRequestNullToCurrencyTest() {
        var request = createConvertCurrencyRequest("RUB", null, 1d);
        var error = webClient.post()
                .uri(URL)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(error.getMessage()).isEqualTo("Поле toCurrency: must not be blank");
    }

    @Test
    void convertCurrencyBadRequestNullFromCurrencyTest() {
        var request = createConvertCurrencyRequest(null, "RUB", 1d);
        var error = webClient.post()
                .uri(URL)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(error.getMessage()).isEqualTo("Поле fromCurrency: must not be blank");
    }

    @Test
    void convertCurrencyBadRequestNegativeAmountTest() {
        var request = createConvertCurrencyRequest("USD", "RUB", -1d);
        var error = webClient.post()
                .uri(URL)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(error.getMessage()).isEqualTo("Поле amount: must be greater than or equal to 0");
    }


    private ConvertCurrencyRequest createConvertCurrencyRequest(String fromCurrency, String toCurrency, Double amount) {
        var response = new ConvertCurrencyRequest();
        response.setFromCurrency(fromCurrency);
        response.setToCurrency(toCurrency);
        response.setAmount(amount);
        return response;
    }
}
