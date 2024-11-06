package ru.mastkey.fj_2024.lesson5.memento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "event_snapshot")
public class EventSnapshot extends Snapshot {
    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "place_id")
    private UUID placeId;
}
