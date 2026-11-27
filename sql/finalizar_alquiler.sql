BEGIN;

ALTER TABLE findimo.tt_solicitud_alquiler
    ADD COLUMN IF NOT EXISTS reserva_finalizada_en timestamptz;

ALTER TABLE findimo.tt_solicitud_alquiler
    DROP CONSTRAINT IF EXISTS tt_solicitud_alquiler_estado_reserva_check;

ALTER TABLE findimo.tt_solicitud_alquiler
    ADD CONSTRAINT tt_solicitud_alquiler_estado_reserva_check
    CHECK (estado_reserva IN ('PENDIENTE', 'CONFIRMADA', 'RECHAZADA', 'FINALIZADA'));

ALTER TABLE findimo.tt_resena
    ADD COLUMN IF NOT EXISTS idsolicitudalquiler bigint
    REFERENCES findimo.tt_solicitud_alquiler (idsolicitudalquiler);

CREATE UNIQUE INDEX IF NOT EXISTS ux_resena_estudiante_por_alquiler
    ON findimo.tt_resena (idsolicitudalquiler)
    WHERE idsolicitudalquiler IS NOT NULL;

COMMIT;
