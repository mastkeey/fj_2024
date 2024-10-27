package ru.mastkey.fj_2024.lesson5.memento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mastkey.fj_2024.lesson5.memento.PlaceSnapshot;

import java.util.List;
import java.util.UUID;

public interface PlaceSnapshotRepository extends JpaRepository<PlaceSnapshot, UUID> {
    List<PlaceSnapshot> findByPlaceId(UUID placeId);
}
