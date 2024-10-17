package ru.mastkey.fj_2024.lesson5.util;

import lombok.experimental.UtilityClass;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoEventsResponse;
import ru.mastkey.fj_2024.lesson5.controller.dto.ConvertCurrencyRequest;

import java.util.regex.Pattern;

@UtilityClass
public class EventServiceUtil {
    public ConvertCurrencyRequest createConvertRequest(Double amount, String currency) {
        var request = new ConvertCurrencyRequest();
        request.setAmount(amount);
        request.setFromCurrency(currency);
        request.setToCurrency("RUB");
        return request;
    }

    public boolean isEventWithinBudget(KudaGoEventsResponse event, Double convertedBudget) {
        Double eventPrice = getPriceFromString(event.getPrice());
        return eventPrice != null && eventPrice <= convertedBudget;
    }

    public Double getPriceFromString(String price) {
        if (price == null || price.isEmpty()) {
            return null;
        }

        var pattern = Pattern.compile("\\d+");
        var matcher = pattern.matcher(price);

        if (matcher.find()) {
            return Double.valueOf(matcher.group());
        }

        return null;
    }
}
