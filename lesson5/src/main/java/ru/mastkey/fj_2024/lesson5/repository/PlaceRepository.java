package ru.mastkey.fj_2024.lesson5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.mastkey.fj_2024.lesson5.entity.Place;

import java.util.Optional;
import java.util.UUID;

public interface PlaceRepository extends JpaRepository<Place, UUID> {
    @Query("SELECT p FROM Place p LEFT JOIN FETCH p.events WHERE p.id = :id")
    Optional<Place> findById(@Param("id") UUID uuid);

    Optional<Place> findByNameAndAddressAndCity(String placeName, String address, String city);
}
