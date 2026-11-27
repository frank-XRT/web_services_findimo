ALTER TABLE findimo.tt_solicitud_alquiler
    ADD COLUMN IF NOT EXISTS reserva_iniciada_por_arrendador boolean NOT NULL DEFAULT false;
