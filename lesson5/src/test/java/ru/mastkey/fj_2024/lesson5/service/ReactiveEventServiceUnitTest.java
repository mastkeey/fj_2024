package ru.mastkey.fj_2024.lesson5.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mastkey.fj_2024.lesson5.client.EventClient;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoEventsResponse;
import ru.mastkey.fj_2024.lesson5.controller.dto.ConvertCurrencyResponse;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ReactiveEventServiceUnitTest {

    @Mock
    private CurrencyService currencyService;

    @Mock
    private EventClient eventClient;

    @InjectMocks
    private ReactiveEventService reactiveEventService;

    private Double budget;
    private String currency;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        budget = 100.0;
        currency = "USD";
        dateFrom = LocalDate.of(2024, 1, 1);
        dateTo = LocalDate.of(2024, 1, 7);
    }

    @Test
    void getEventsByBudget_ShouldReturnFilteredEvents_WhenEventsExistWithinBudget() {
        ConvertCurrencyResponse mockCurrencyResponse = new ConvertCurrencyResponse();
        mockCurrencyResponse.setConvertedAmount("7500.0");

        KudaGoEventsResponse event1 = new KudaGoEventsResponse();
        event1.setPrice("5000");

        KudaGoEventsResponse event2 = new KudaGoEventsResponse();
        event2.setPrice("10000");

        when(currencyService.convertCurrencyMono(any())).thenReturn(Mono.just(mockCurrencyResponse));
        when(eventClient.getEventsFlux(dateFrom, dateTo)).thenReturn(Flux.just(event1, event2));

        List<KudaGoEventsResponse> result = reactiveEventService
                .getEventsByBudget(budget, currency, dateFrom, dateTo)
                .block();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(event1, result.get(0));
    }

    @Test
    void getEventsByBudget_ShouldReturnEmptyList_WhenNoEventsWithinBudget() {
        ConvertCurrencyResponse mockCurrencyResponse = new ConvertCurrencyResponse();
        mockCurrencyResponse.setConvertedAmount("500.0");

        KudaGoEventsResponse event1 = new KudaGoEventsResponse();
        event1.setPrice("5000");

        when(currencyService.convertCurrencyMono(any())).thenReturn(Mono.just(mockCurrencyResponse));
        when(eventClient.getEventsFlux(dateFrom, dateTo)).thenReturn(Flux.just(event1));

        List<KudaGoEventsResponse> result = reactiveEventService
                .getEventsByBudget(budget, currency, dateFrom, dateTo)
                .block();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getEventsByBudget_ShouldHandleEmptyEventList() {
        ConvertCurrencyResponse mockCurrencyResponse = new ConvertCurrencyResponse();
        mockCurrencyResponse.setConvertedAmount("1000.0");

        when(currencyService.convertCurrencyMono(any())).thenReturn(Mono.just(mockCurrencyResponse));
        when(eventClient.getEventsFlux(dateFrom, dateTo)).thenReturn(Flux.empty());

        List<KudaGoEventsResponse> result = reactiveEventService
                .getEventsByBudget(budget, currency, dateFrom, dateTo)
                .block();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getEventsByBudget_ShouldReturnEvents_WhenDatesAreNull() {
        ConvertCurrencyResponse mockCurrencyResponse = new ConvertCurrencyResponse();
        mockCurrencyResponse.setConvertedAmount("1000.0");

        KudaGoEventsResponse event1 = new KudaGoEventsResponse();
        event1.setPrice("800");

        when(currencyService.convertCurrencyMono(any())).thenReturn(Mono.just(mockCurrencyResponse));
        when(eventClient.getEventsFlux(any(LocalDate.class), any(LocalDate.class))).thenReturn(Flux.just(event1));

        List<KudaGoEventsResponse> result = reactiveEventService
                .getEventsByBudget(budget, currency, null, null)
                .block();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(event1, result.get(0));
    }
}