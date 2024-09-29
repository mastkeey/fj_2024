package ru.mastkey.fj_2024.lesson5.client;

import java.util.List;

public interface ApiClient <E> {
    List<E> getAllEntitiesFromKudaGo();
}
