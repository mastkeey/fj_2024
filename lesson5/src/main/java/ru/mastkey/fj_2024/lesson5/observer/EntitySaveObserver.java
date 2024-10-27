package ru.mastkey.fj_2024.lesson5.observer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EntitySaveObserver {

    @EventListener
    public <T> void handleEntitySavedEvent(SaveEntityEvent<T> event) {
        T entity = event.getEntity();
        var type = event.getType();

        switch (type) {
            case CREATED -> log.info("Entity saved: {}", entity);
            case UPDATED ->log.info("Entity updated: {}", entity);
        }
    }
}