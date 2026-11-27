BEGIN;

CREATE UNIQUE INDEX IF NOT EXISTS ux_resena_por_autor_y_alquiler
    ON findimo.tt_resena (idsolicitudalquiler, idusuarioautor)
    WHERE idsolicitudalquiler IS NOT NULL;

DROP INDEX IF EXISTS findimo.ux_resena_estudiante_por_alquiler;

COMMIT;
