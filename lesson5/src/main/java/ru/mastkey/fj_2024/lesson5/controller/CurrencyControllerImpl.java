package ru.mastkey.fj_2024.lesson5.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.mastkey.fj_2024.lesson5.controller.dto.ConvertCurrencyRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.ConvertCurrencyResponse;
import ru.mastkey.fj_2024.lesson5.controller.dto.CurrencyRateResponse;
import ru.mastkey.fj_2024.lesson5.service.CurrencyService;

@RequiredArgsConstructor
@RestController
public class CurrencyControllerImpl implements CurrencyController {

    private final CurrencyService currencyService;

    @Override
    public ResponseEntity<CurrencyRateResponse> getCurrencyRate(String charCode) {
        return ResponseEntity.ok(currencyService.getCurrencyRate(charCode));
    }

    @Override
    public ResponseEntity<ConvertCurrencyResponse> convertCurrency(ConvertCurrencyRequest request) {
        return ResponseEntity.ok(currencyService.convertCurrency(request));
    }
}
