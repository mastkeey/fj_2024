--liquibase formatted sql

--changeset fetyukhin:event_snapshot

CREATE TABLE event_snapshot
(
    id         UUID PRIMARY KEY,
    event_id   UUID NOT NULL,
    name       VARCHAR(255),
    date       DATE,
    place_id   UUID,
    created_at TIMESTAMP NOT NULL
);