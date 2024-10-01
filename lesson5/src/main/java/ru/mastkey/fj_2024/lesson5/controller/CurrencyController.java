package ru.mastkey.fj_2024.lesson5.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mastkey.fj_2024.lesson5.controller.dto.ConvertCurrencyRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.ConvertCurrencyResponse;
import ru.mastkey.fj_2024.lesson5.controller.dto.CurrencyRateResponse;
import ru.mastkey.fj_2024.lesson5.exception.response.ErrorResponse;

@RequestMapping("/currencies")
public interface CurrencyController {
    @Operation(
            summary = "Получить курс валюты по коду",
            description = "Возвращает текущий курс валюты по указанному буквенному коду (например, USD, EUR)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Курс успешно получен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CurrencyRateResponse.class),
                            examples = {
                                    @ExampleObject(value = "{\"currency\": \"USD\", \"rate\": \"94.15\", \"name\": \"Доллар США\"}")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Валюта с указанным кодом не найдена в ЦБ",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(value = "{\"status\": \"404\", \"code\": \"BadRequest\", \"message\": \"Unsupported currency code - THB\"}")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Несуществующий формат кода валюты",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(value = "{\"status\": \"400\", \"code\": \"BadRequest\", \"message\": \"Non-existent currency code - NONNN\"}")
                            }
                    )
            )

    })
    @GetMapping("/rates/{charCode}")
    ResponseEntity<CurrencyRateResponse> getCurrencyRate(
            @Parameter(description = "Буквенный код валюты (например, USD, EUR)", example = "USD")
            @PathVariable String charCode
    );

    @Operation(
            summary = "Конвертировать валюту",
            description = "Конвертирует указанную сумму из одной валюты в другую на основе актуального курса."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Конвертация успешно выполнена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConvertCurrencyResponse.class),
                            examples = {
                                    @ExampleObject(value = "{\"fromCurrency\": \"USD\", \"toCurrency\": \"RUB\", \"convertedAmount\": \"94.4\"}")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Валюта с указанным кодом не найдена в ЦБ",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(value = "{\"status\": \"404\", \"code\": \"BadRequest\", \"message\": \"Unsupported currency code - THB\"}")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Одна из указанных не существует или невалидное тело запроса",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(value = "{\"status\": \"400\", \"code\": \"BadRequest\", \"message\": \"Поле amount: must be greater than or equal to 0\"}")
                            }
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Запрос на конвертацию валюты с указанием исходной валюты, целевой валюты и суммы.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ConvertCurrencyRequest.class),
                    examples = @ExampleObject(value = "{\"fromCurrency\": \"USD\", \"toCurrency\": \"EUR\", \"amount\": 100.0}")
            )
    )
    @PostMapping("/convert")
    public ResponseEntity<ConvertCurrencyResponse> convertCurrency(@RequestBody @Valid ConvertCurrencyRequest request);
}
