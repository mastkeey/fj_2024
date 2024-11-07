--liquibase formatted sql

-- changeset fetyukhin:roles-data

INSERT INTO role(id, name) VALUES ('536135ca-d4f4-4039-830f-f83426f6394d', 'USER');
INSERT INTO role(id, name) VALUES ('8b5d9d9e-9274-4893-bebd-d2e6f4dc0f7b', 'ADMIN');