package ru.mastkey.fj_2024.lesson5.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.mastkey.fj_2024.lesson5.client.EventClient;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoEventsResponse;
import ru.mastkey.fj_2024.lesson5.controller.dto.ConvertCurrencyResponse;
import ru.mastkey.fj_2024.lesson5.exception.ErrorType;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;
import ru.mastkey.fj_2024.lesson5.util.EventServiceUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompletableFutureEventService {

    private static final String CURRENCY_ERROR = "Error in currency conversion: %s";
    private static final String EVENT_ERROR = "Error in getting events: %s";
    private static final String COMBINE_ERROR = "Error in combining results: %s";

    private final CurrencyService currencyService;
    private final EventClient eventClient;
    @Qualifier("eventServiceThreadPool")
    private final ExecutorService eventServiceThreadPool;

    public CompletableFuture<List<KudaGoEventsResponse>> getEventsByBudget(Double budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        LocalDate now = LocalDate.now();
        if (Objects.isNull(dateFrom)) {
            dateFrom = now.with(DayOfWeek.MONDAY);
        }
        if (Objects.isNull(dateTo)) {
            dateTo = now.with(DayOfWeek.SUNDAY);
        }

        LocalDate finalDateFrom = dateFrom;
        LocalDate finalDateTo = dateTo;

        CompletableFuture<ConvertCurrencyResponse> currencyFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return currencyService.convertCurrency(EventServiceUtil.createConvertRequest(budget, currency));
            } catch (Exception e) {
                log.error(String.format(CURRENCY_ERROR , e.getMessage()));
                throw new ServiceException(ErrorType.INTERNAL_SERVER_ERROR, CURRENCY_ERROR, e.getMessage());
            }
        }, eventServiceThreadPool);

        CompletableFuture<List<KudaGoEventsResponse>> eventsFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return eventClient.getEvents(finalDateFrom, finalDateTo);
            } catch (Exception e) {
                log.error(String.format(EVENT_ERROR , e.getMessage()));
                throw new ServiceException(ErrorType.INTERNAL_SERVER_ERROR, EVENT_ERROR, e.getMessage());
            }
        }, eventServiceThreadPool);

        return currencyFuture.thenCombine(eventsFuture, (currencyResponse, events) -> {
            return events.stream()
                    .filter(event -> EventServiceUtil.isEventWithinBudget(event,
                            EventServiceUtil.getPriceFromString(currencyResponse.getConvertedAmount())))
                    .collect(Collectors.toList());
        }).exceptionally(ex -> {
            log.error(String.format(COMBINE_ERROR, ex.getMessage()));
            return List.of();
        });
    }
}