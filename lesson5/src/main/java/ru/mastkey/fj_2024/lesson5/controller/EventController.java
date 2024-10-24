package ru.mastkey.fj_2024.lesson5.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mastkey.fj_2024.lesson5.controller.dto.EventRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.EventResponse;
import ru.mastkey.fj_2024.lesson5.service.EventService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents(@RequestParam(value = "filtering", required = false) String filtering) {
        return ResponseEntity.ok(eventService.getAllEvents(filtering));
    }

    @GetMapping ("/{id}")
    ResponseEntity<EventResponse> getEventById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PostMapping
    ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        return ResponseEntity.ok(eventService.createEvent(request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteEventById(@PathVariable UUID id) {
        eventService.deleteEventById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    ResponseEntity<EventResponse> updateEventById(@PathVariable UUID id, @Valid @RequestBody EventRequest event) {
        return ResponseEntity.ok(eventService.updateEvent(id, event));
    }
}
