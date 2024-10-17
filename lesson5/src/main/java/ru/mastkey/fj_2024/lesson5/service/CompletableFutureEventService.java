package ru.mastkey.fj_2024.lesson5.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mastkey.fj_2024.lesson5.client.EventClient;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoEventsResponse;
import ru.mastkey.fj_2024.lesson5.controller.dto.ConvertCurrencyResponse;
import ru.mastkey.fj_2024.lesson5.util.EventServiceUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompletableFutureEventService {

    private final CurrencyService currencyService;
    private final EventClient eventClient;

    public CompletableFuture<List<KudaGoEventsResponse>> getEventsByBudget(Double budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        var now = LocalDate.now();
        if (Objects.isNull(dateFrom)) {
            dateFrom = now.with(DayOfWeek.MONDAY);
        }
        if (Objects.isNull(dateTo)) {
            dateTo = now.with(DayOfWeek.SUNDAY);
        }

        CompletableFuture<ConvertCurrencyResponse> currencyFuture = CompletableFuture.supplyAsync(() ->
            currencyService.convertCurrency(EventServiceUtil.createConvertRequest(budget, currency))
        );

        LocalDate finalDateTo = dateTo;
        LocalDate finalDateFrom = dateFrom;
        CompletableFuture<List<KudaGoEventsResponse>> eventsFuture = CompletableFuture.supplyAsync(() ->
            eventClient.getEvents(finalDateFrom, finalDateTo)
        );

        return currencyFuture.thenCombine(eventsFuture, (currencyResponse, events) -> 
            events.stream()
                  .filter(event -> EventServiceUtil.isEventWithinBudget(event, EventServiceUtil.getPriceFromString(currencyResponse.getConvertedAmount())))
                  .collect(Collectors.toList())
        );
    }
}