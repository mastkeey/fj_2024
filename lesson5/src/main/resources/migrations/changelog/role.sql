--liquibase formatted sql

--changeset fetyukhin:role

CREATE TABLE role
(
    id   UUID primary key,
    name text not null
)
