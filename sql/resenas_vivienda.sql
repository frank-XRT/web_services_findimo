BEGIN;

CREATE UNIQUE INDEX IF NOT EXISTS ux_resena_personal_por_alquiler
    ON findimo.tt_resena (idsolicitudalquiler, idusuarioautor)
    WHERE idsolicitudalquiler IS NOT NULL AND idusuarioobjetivo IS NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS ux_resena_vivienda_por_alquiler
    ON findimo.tt_resena (idsolicitudalquiler, idusuarioautor)
    WHERE idsolicitudalquiler IS NOT NULL AND idusuarioobjetivo IS NULL;

DROP INDEX IF EXISTS findimo.ux_resena_por_autor_y_alquiler;
DROP INDEX IF EXISTS findimo.ux_resena_estudiante_por_alquiler;

COMMIT;
