ALTER TABLE findimo.tm_propiedad
    ADD COLUMN IF NOT EXISTS servicios_personalizados jsonb NOT NULL DEFAULT '[]'::jsonb;
