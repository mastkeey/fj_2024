package ru.mastkey.fj_2024.lesson5.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.mastkey.fj_2024.lesson5.client.CBClient;
import ru.mastkey.fj_2024.lesson5.client.dto.CBCurrencyResponse;
import ru.mastkey.fj_2024.lesson5.controller.dto.ConvertCurrencyRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.ConvertCurrencyResponse;
import ru.mastkey.fj_2024.lesson5.controller.dto.CurrencyRateResponse;
import ru.mastkey.fj_2024.lesson5.exception.ErrorType;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private static final String MSG_CURRENCY_NOT_SUPPORTED = "Unsupported currency code - %s";
    private static final String MSG_CURRENCY_NON_EXIST = "Non-existent currency code - %s";
    private static final String RUB_CODE = "RUB";

    private final ConversionService conversionService;
    private final CBClient cBClient;

    public CurrencyRateResponse getCurrencyRate(String code) {
        validateCurrencyCode(code);

        var currencies = cBClient.getCurrencies();

        return conversionService.convert(getCurrencyByCode(code, currencies), CurrencyRateResponse.class);
    }

    public ConvertCurrencyResponse convertCurrency(ConvertCurrencyRequest request) {
        validateConvertRequest(request);

        var currencies = cBClient.getCurrencies();

        CBCurrencyResponse fromCurrency;
        CBCurrencyResponse toCurrency;

        if (Objects.equals(request.getFromCurrency(), RUB_CODE)) {
            toCurrency = getCurrencyByCode(request.getToCurrency(), currencies);
            var convertedAmount = request.getAmount() / Double.parseDouble(toCurrency.getVunitRate().replace(",","."));
            return createConvertCurrencyResponse(request, convertedAmount);
        }

        if (Objects.equals(request.getToCurrency(), RUB_CODE)) {
            fromCurrency = getCurrencyByCode(request.getFromCurrency(), currencies);
            var convertedAmount = Double.parseDouble(fromCurrency.getVunitRate().replace(",",".")) * request.getAmount();
            return createConvertCurrencyResponse(request, convertedAmount);
        }

        fromCurrency = getCurrencyByCode(request.getFromCurrency(), currencies);
        toCurrency = getCurrencyByCode(request.getToCurrency(), currencies);

        var convertedAmount = convert(request.getAmount(), fromCurrency, toCurrency);

        return createConvertCurrencyResponse(request, convertedAmount);
    }

    private void validateConvertRequest(ConvertCurrencyRequest request) {
        validateCurrencyCode(request.getFromCurrency());
        validateCurrencyCode(request.getToCurrency());
    }

    private void validateCurrencyCode(String code) {
        try {
            Currency.getInstance(code);
        } catch (IllegalArgumentException e) {
            throw new ServiceException(ErrorType.BAD_REQUEST, MSG_CURRENCY_NON_EXIST, code);
        }
    }

    private ConvertCurrencyResponse createConvertCurrencyResponse(ConvertCurrencyRequest request, Double convertedAmount) {
        var response = new ConvertCurrencyResponse();
        response.setFromCurrency(request.getFromCurrency());
        response.setToCurrency(request.getToCurrency());
        response.setConvertedAmount(new BigDecimal(convertedAmount).setScale(2, RoundingMode.HALF_UP).toPlainString());
        return response;
    }

    private Double convert(Double amount, CBCurrencyResponse fromCurrency, CBCurrencyResponse toCurrency) {
        var fromValueInRub = Double.parseDouble(fromCurrency.getValue().replace(",",".")) / fromCurrency.getNominal();
        var toValueInRub = Double.parseDouble(toCurrency.getValue().replace(",",".")) / toCurrency.getNominal();

        var amountInRub = amount * fromValueInRub;

        return amountInRub / toValueInRub;
    }

    private CBCurrencyResponse getCurrencyByCode(String code, List<CBCurrencyResponse> currencies) {
        return currencies.stream()
                .filter(cbCurrencyResponse -> Objects.equals(cbCurrencyResponse.getCharCode(), code))
                .findFirst()
                .orElseThrow(
                        () -> new ServiceException(ErrorType.NOT_FOUND, MSG_CURRENCY_NOT_SUPPORTED, code)
                );
    }
}
