package ru.mastkey.fj_2024.lesson5.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mastkey.fj_2024.lesson5.client.EventClient;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoEventsResponse;
import ru.mastkey.fj_2024.lesson5.controller.dto.ConvertCurrencyRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.ConvertCurrencyResponse;
import ru.mastkey.fj_2024.lesson5.util.EventServiceUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactiveEventService {

    private final CurrencyService currencyService;
    private final EventClient eventClient;

    public Mono<List<KudaGoEventsResponse>> getEventsByBudget(Double budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        var now = LocalDate.now();
        if (Objects.isNull(dateFrom)) {
            dateFrom = now.with(DayOfWeek.MONDAY);
        }
        if (Objects.isNull(dateTo)) {
            dateTo = now.with(DayOfWeek.SUNDAY);
        }

        Mono<ConvertCurrencyResponse> currencyMono = currencyService.convertCurrencyMono(EventServiceUtil.createConvertRequest(budget, currency));
        Flux<KudaGoEventsResponse> eventsFlux = eventClient.getEventsFlux(dateFrom, dateTo);

        return Mono.zip(currencyMono, eventsFlux.collectList(), (currencyResponse, events) ->
            events.stream()
                  .filter(event -> EventServiceUtil.isEventWithinBudget(event, EventServiceUtil.getPriceFromString(currencyResponse.getConvertedAmount())))
                  .collect(Collectors.toList())
        );
    }
}