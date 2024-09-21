package ru.mastkey.fj_2024.lesson5.repository;

import java.util.List;
import java.util.Optional;

public interface EntityRepository <I, E>{
    Optional<E> findById(I id);
    List<E> findAll();
    E save(E entity);
    void saveAll(List<E> entities);
    void delete(E entity);
    void deleteById(I id);
    void deleteAll();
}
