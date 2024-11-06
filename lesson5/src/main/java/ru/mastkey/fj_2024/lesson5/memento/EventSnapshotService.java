package ru.mastkey.fj_2024.lesson5.memento;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mastkey.fj_2024.lesson5.entity.Event;
import ru.mastkey.fj_2024.lesson5.memento.repository.EventSnapshotRepository;

@Service
@RequiredArgsConstructor
public class EventSnapshotService {
    private final EventSnapshotRepository snapshotRepository;

    @Transactional
    public void saveSnapshot(Event event) {
        EventSnapshot snapshot = new EventSnapshot();
        snapshot.setEventId(event.getId());
        snapshot.setName(event.getName());
        snapshot.setDate(event.getDate());
        snapshot.setPlaceId(event.getPlace().getId());
        snapshotRepository.save(snapshot);
    }
}