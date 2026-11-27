BEGIN;

ALTER TABLE findimo.tt_solicitud_alquiler
    ADD COLUMN IF NOT EXISTS estado_reserva varchar(12);

CREATE UNIQUE INDEX IF NOT EXISTS ux_reserva_confirmada_por_vivienda
    ON findimo.tt_solicitud_alquiler (idpropiedad)
    WHERE estado_reserva = 'CONFIRMADA';

COMMIT;
