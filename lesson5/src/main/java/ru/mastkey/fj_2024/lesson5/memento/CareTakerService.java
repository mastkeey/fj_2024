package ru.mastkey.fj_2024.lesson5.memento;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mastkey.fj_2024.lesson5.exception.ErrorType;
import ru.mastkey.fj_2024.lesson5.exception.ServiceException;
import ru.mastkey.fj_2024.lesson5.memento.repository.EventSnapshotRepository;
import ru.mastkey.fj_2024.lesson5.memento.repository.PlaceSnapshotRepository;
import ru.mastkey.fj_2024.lesson5.repository.EventRepository;
import ru.mastkey.fj_2024.lesson5.repository.PlaceRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CareTakerService {

    private final EventSnapshotRepository eventSnapshotRepository;
    private final PlaceSnapshotRepository placeSnapshotRepository;
    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public void restoreEvent(UUID eventId, UUID snapshotId) {
        var snapshot = eventSnapshotRepository.findById(snapshotId)
                .orElseThrow(
                        () -> new ServiceException(ErrorType.NOT_FOUND, "Snapshot not found")
                );

        var event = eventRepository.findById(eventId)
                .orElseThrow(
                        () -> new ServiceException(ErrorType.NOT_FOUND, "Event not found")
                );

        var place = placeRepository.findById(snapshot.getPlaceId())
                .orElseThrow(
                        () -> new ServiceException(ErrorType.NOT_FOUND, "Place not found")
                );


        event.setName(snapshot.getName());
        event.setDate(snapshot.getDate());
        event.setPlace(place);

        eventRepository.save(event);
    }

    @Transactional
    public void restorePlace(UUID placeId, UUID snapshotId) {
        var snapshot = placeSnapshotRepository.findById(snapshotId)
                .orElseThrow(
                        () -> new ServiceException(ErrorType.NOT_FOUND, "Snapshot not found")
                );

        var place = placeRepository.findById(placeId)
                .orElseThrow(
                        () -> new ServiceException(ErrorType.NOT_FOUND, "Place not found")
                );

        place.setName(snapshot.getName());
        place.setAddress(snapshot.getAddress());
        place.setCity(snapshot.getCity());

        placeRepository.save(place);
    }

    @Transactional(readOnly = true)
    public List<EventSnapshot> getEventHistory(UUID eventId) {
        return eventSnapshotRepository.findByEventId(eventId);
    }

    @Transactional(readOnly = true)
    public List<PlaceSnapshot> getPlaceHistory(UUID placeId) {
        return placeSnapshotRepository.findByPlaceId(placeId);
    }
}