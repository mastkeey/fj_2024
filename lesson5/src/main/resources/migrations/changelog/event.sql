--liquibase formatted sql

--changeset fetyukhin:event

CREATE TABLE event
(
    id       UUID PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    date     DATE         NOT NULL,
    place_id UUID,
    CONSTRAINT fk_event_place FOREIGN KEY (place_id) REFERENCES place (id)
);