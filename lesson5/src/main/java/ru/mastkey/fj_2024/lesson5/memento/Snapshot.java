package ru.mastkey.fj_2024.lesson5.memento;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@MappedSuperclass
public abstract class Snapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDate.now();
    }
}
