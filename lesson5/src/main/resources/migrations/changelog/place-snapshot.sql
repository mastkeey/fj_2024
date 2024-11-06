--liquibase formatted sql

--changeset fetyukhin:place_snapshot

CREATE TABLE place_snapshot
(
    id         UUID PRIMARY KEY,
    place_id   UUID NOT NULL,
    name       VARCHAR(255),
    address    VARCHAR(255),
    city       VARCHAR(255),
    created_at TIMESTAMP NOT NULL
);
