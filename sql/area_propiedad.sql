ALTER TABLE findimo.tm_propiedad
    ADD COLUMN IF NOT EXISTS area_m2 numeric(8, 2)
    CHECK (area_m2 > 0);
