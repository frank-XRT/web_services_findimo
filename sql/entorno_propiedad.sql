-- PostgreSQL. Ejecutar en la base de datos que usa el backend de Findimo.
BEGIN;

ALTER TABLE findimo.tm_propiedad
    ADD COLUMN IF NOT EXISTS entorno jsonb;

-- Cada fila de VALUES actualiza exclusivamente la vivienda de ese ID.
-- Sustituye NULL::bigint por el ID real y ajusta los lugares y tiempos.
-- Para cargar otra vivienda, añade otra fila con su propio ID y arreglo.
-- El arreglo reemplaza el entorno anterior de la vivienda indicada.
UPDATE findimo.tm_propiedad AS propiedad
SET entorno = datos.entorno
FROM (
    VALUES (
        NULL::bigint,
        '[
            {"tipo": "UNIVERSIDAD", "nombre": "UPC", "minutos": 8},
            {"tipo": "MERCADO", "nombre": null, "minutos": 5},
            {"tipo": "BODEGA", "nombre": null, "minutos": 2},
            {"tipo": "PARQUE", "nombre": "Parque del barrio", "minutos": 6},
            {"tipo": "SEGURIDAD", "nombre": null, "minutos": 3},
            {"tipo": "TRANSPORTE", "nombre": "Paradero principal", "minutos": 4}
        ]'::jsonb
    )
) AS datos(idpropiedad, entorno)
WHERE propiedad.idpropiedad = datos.idpropiedad
RETURNING propiedad.idpropiedad, propiedad.titulo, propiedad.entorno;

COMMIT;
