package ru.mastkey.fj_2024.lesson5.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import ru.mastkey.fj_2024.lesson5.entity.Location;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LocationRepository implements EntityRepository<UUID, Location>{

    private final Map<UUID, Location> locations = new ConcurrentHashMap<>();

    @Override
    public Optional<Location> findById(UUID id) {
        return locations.containsKey(id)
                ? Optional.of(locations.get(id))
                : Optional.empty();
    }

    @Override
    public List<Location> findAll() {
        return locations.values().stream().toList();
    }

    @Override
    public Location save(Location entity) {
        if (Objects.isNull(entity.getId())) {
            entity.setId(UUID.randomUUID());
        }
        locations.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void saveAll(List<Location> entities) {
        entities.forEach(this::save);
    }

    @Override
    public void delete(Location entity) {
        locations.remove(entity.getId());
    }

    @Override
    public void deleteById(UUID id) {
        locations.remove(id);
    }

    @Override
    public void deleteAll() {
        locations.clear();
    }
}
