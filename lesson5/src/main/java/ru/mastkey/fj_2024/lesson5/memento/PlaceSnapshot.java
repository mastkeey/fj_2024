package ru.mastkey.fj_2024.lesson5.memento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "place_snapshot")
public class PlaceSnapshot extends Snapshot {
    @Column(name = "place_id", nullable = false)
    private UUID placeId;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;
}
