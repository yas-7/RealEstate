--liquibase formatted sql

--changeset re:001-create-complexes
CREATE TABLE complexes (
  id          BIGSERIAL PRIMARY KEY,
  name        TEXT NOT NULL,
  city        TEXT,
  address     TEXT,
  developer   TEXT,
  created_at  TIMESTAMP NOT NULL DEFAULT now()
);

--changeset re:002-create-flats
CREATE TABLE flats (
  id           BIGSERIAL PRIMARY KEY,
  complex_id   BIGINT NOT NULL REFERENCES complexes(id) ON DELETE CASCADE,
  building     TEXT NOT NULL,
  number       TEXT NOT NULL,
  floor        INT  NOT NULL,
  rooms        INT  NOT NULL,
  area_total   NUMERIC(8,2) NOT NULL,
  actual_price_total    INT NOT NULL,
  actual_price_per_m2   INT NOT NULL,
  created_at   TIMESTAMP NOT NULL DEFAULT now(),
  CONSTRAINT uq_flats_complex_building_number UNIQUE (complex_id, building, number)
);

--changeset re:003-create-flat-price-history
CREATE TABLE flat_price_history (
  id             BIGSERIAL PRIMARY KEY,
  flat_id        BIGINT NOT NULL REFERENCES flats(id) ON DELETE CASCADE,
  price_total    INT NOT NULL,
  price_per_m2   INT NOT NULL,
  created_at     TIMESTAMP NOT NULL DEFAULT now()
);

--changeset re:004-indexes
CREATE INDEX idx_flats_complex_building ON flats (complex_id, building);
CREATE INDEX idx_price_hist_flat_created_at ON flat_price_history (flat_id, created_at);
CREATE INDEX idx_price_hist_price_total ON flat_price_history (price_total, created_at);
CREATE INDEX idx_price_hist_price_per_m2 ON flat_price_history (price_per_m2, created_at);
