package ru.mastkey.fj_2024.lesson5.memento;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mastkey.fj_2024.lesson5.entity.Place;
import ru.mastkey.fj_2024.lesson5.memento.repository.PlaceSnapshotRepository;

@Service
@RequiredArgsConstructor
public class PlaceSpanshotService {
    private final PlaceSnapshotRepository snapshotRepository;

    @Transactional
    public void saveSnapshot(Place place) {
        PlaceSnapshot snapshot = new PlaceSnapshot();
        snapshot.setPlaceId(place.getId());
        snapshot.setName(place.getName());
        snapshot.setAddress(place.getAddress());
        snapshot.setCity(place.getCity());
        snapshotRepository.save(snapshot);
    }
}