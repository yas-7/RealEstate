--liquibase formatted sql

--changeset re:005-add-column-modified-at-to-flats
ALTER TABLE flats ADD COLUMN modified_at TIMESTAMP NOT NULL DEFAULT now();