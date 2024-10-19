--liquibase formatted sql

--changeset fetyukhin:place

CREATE TABLE place
(
    id      UUID PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city    VARCHAR(255) NOT NULL
);