package ru.mastkey.fj_2024.lesson5.memento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mastkey.fj_2024.lesson5.memento.EventSnapshot;

import java.util.List;
import java.util.UUID;

public interface EventSnapshotRepository extends JpaRepository<EventSnapshot, UUID> {
    List<EventSnapshot> findByEventId(UUID eventId);
}
