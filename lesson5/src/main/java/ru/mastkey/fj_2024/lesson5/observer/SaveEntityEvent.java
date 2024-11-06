package ru.mastkey.fj_2024.lesson5.observer;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SaveEntityEvent<T> extends ApplicationEvent {
    private final T entity;
    private final SaveEntityEventType type;

    public SaveEntityEvent(Object source, T entity, SaveEntityEventType type) {
        super(source);
        this.entity = entity;
        this.type = type;
    }
}