package ru.mastkey.fj_2024.lesson5.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import ru.mastkey.fj_2024.lesson5.entity.Category;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CategoryRepository implements EntityRepository<UUID, Category>{

    private final Map<UUID, Category> categories = new ConcurrentHashMap<>();

    @Override
    public Optional<Category> findById(UUID id) {
        return categories.containsKey(id)
                ? Optional.of(categories.get(id))
                : Optional.empty();
    }

    @Override
    public List<Category> findAll() {
        return categories.values().stream().toList();
    }

    @Override
    public Category save(Category entity) {
        if (Objects.isNull(entity.getId())) {
            entity.setId(UUID.randomUUID());
        }
        categories.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void saveAll(List<Category> entities) {
        entities.forEach(this::save);
    }

    @Override
    public void delete(Category entity) {
        categories.remove(entity.getId());
    }

    @Override
    public void deleteById(UUID id) {
        categories.remove(id);
    }

    @Override
    public void deleteAll() {
        categories.clear();
    }
}
