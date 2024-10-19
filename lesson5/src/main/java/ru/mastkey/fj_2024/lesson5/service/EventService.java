package ru.mastkey.fj_2024.lesson5.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mastkey.fj_2024.lesson5.controller.dto.EventRequest;
import ru.mastkey.fj_2024.lesson5.controller.dto.EventResponse;
import ru.mastkey.fj_2024.lesson5.entity.Event;
import ru.mastkey.fj_2024.lesson5.entity.Place;
import ru.mastkey.fj_2024.lesson5.exception.ErrorType;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;
import ru.mastkey.fj_2024.lesson5.mapper.EventRequestToEventMapper;
import ru.mastkey.fj_2024.lesson5.repository.EventRepository;
import ru.mastkey.fj_2024.lesson5.repository.PlaceRepository;
import ru.mastkey.fj_2024.lesson5.util.FilteringUtils;

import java.util.List;
import java.util.UUID;

import static ru.mastkey.fj_2024.lesson5.util.Constants.MSG_EVENT_NOT_FOUND;
import static ru.mastkey.fj_2024.lesson5.util.Constants.MSG_PLACE_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;
    private final ConversionService conversionService;
    private final EventRequestToEventMapper eventRequestToEventMapper;

    @Transactional
    public EventResponse createEvent(EventRequest request) {
        var place = validateAndGetPlace(request.getPlaceId());

        var event = conversionService.convert(request, Event.class);
        event.setPlace(place);

        return conversionService.convert(eventRepository.save(event), EventResponse.class);
    }

    @Transactional(readOnly = true)
    public EventResponse getEventById(UUID id) {
        var event = validateAndGetEvent(id);

        return conversionService.convert(event, EventResponse.class);
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getAllEvents(String filtering) {
        var filter = FilteringUtils.<Event>getFilter(filtering);

        var events = eventRepository.findAll(filter);

        return events.stream()
                .map(event -> conversionService.convert(event, EventResponse.class))
                .toList();
    }

    @Transactional
    public void deleteEventById(UUID id) {
        validateAndGetEvent(id);
        eventRepository.deleteById(id);
    }

    @Transactional
    public EventResponse updateEvent(UUID id, EventRequest eventRequest) {
        var event = validateAndGetEvent(id);
        var place = validateAndGetPlace(eventRequest.getPlaceId());

        var updatedEvent = eventRequestToEventMapper.toEventForUpdate(event, eventRequest);
        updatedEvent.setPlace(place);
        return conversionService.convert(eventRepository.save(updatedEvent), EventResponse.class);
    }

    private Event validateAndGetEvent(UUID id) {
        var event = eventRepository.findById(id);
        if (event.isEmpty()) {
            throw new ServiceException(ErrorType.NOT_FOUND, MSG_EVENT_NOT_FOUND, id);
        }
        return event.get();
    }

    private Place validateAndGetPlace(UUID id) {
        var place = placeRepository.findById(id);
        if (place.isEmpty()) {
            throw new ServiceException(ErrorType.BAD_REQUEST, MSG_PLACE_NOT_EXIST, id);
        }
        return place.get();
    }
}
