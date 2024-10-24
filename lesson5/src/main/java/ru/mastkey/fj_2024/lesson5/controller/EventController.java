package ru.mastkey.fj_2024.lesson5.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.mastkey.fj_2024.lesson5.client.dto.KudaGoEventsResponse;
import ru.mastkey.fj_2024.lesson5.service.ReactiveEventService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final ReactiveEventService eventService;

    @GetMapping
    public ResponseEntity<Mono<List<KudaGoEventsResponse>>> getEvents(
            @RequestParam("budget") Double budget,
            @RequestParam("currency") String currency,
            @RequestParam(value = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) LocalDate dateTo) {

        return ResponseEntity.ok(eventService.getEventsByBudget(budget, currency, dateFrom, dateTo));
    }
}
