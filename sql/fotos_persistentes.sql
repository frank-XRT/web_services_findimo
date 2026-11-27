CREATE TABLE IF NOT EXISTS findimo.tt_foto_binaria (
    id uuid PRIMARY KEY,
    tipo varchar(20) NOT NULL,
    contenido bytea NOT NULL
);
