--liquibase formatted sql

--changeset re:005-add-column-modified-at-to-flats
ALTER TABLE flats ADD COLUMN modified_at TIMESTAMP NOT NULL DEFAULT now();

--changeset re:006-indexes
CREATE INDEX idx_flats_modified_at ON flats (modified_at);