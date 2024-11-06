--liquibase formatted sql

--changeset fetyukhin:user

CREATE TABLE users(
    id UUID primary key,
    username text not null,
    password text not null
)