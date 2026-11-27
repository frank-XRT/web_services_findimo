CREATE TABLE IF NOT EXISTS findimo.tt_favorito_usuario (
    idusuario bigint NOT NULL REFERENCES findimo.tm_usuario (idusuario) ON DELETE CASCADE,
    idpropiedad bigint NOT NULL REFERENCES findimo.tm_propiedad (idpropiedad) ON DELETE CASCADE,
    PRIMARY KEY (idusuario, idpropiedad)
);
