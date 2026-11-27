package com.findimo.api.Service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
public class FotosBaseDatos {
    public static final String RUTA = "/imagenes/viviendas-db/";
    private final JdbcClient jdbc;

    public record Foto(String tipo, byte[] contenido) {}

    public FotosBaseDatos(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public String guardar(String formato, byte[] contenido) {
        UUID id = UUID.randomUUID();
        jdbc.sql("INSERT INTO findimo.tt_foto_binaria (id, tipo, contenido) VALUES (:id, :tipo, :contenido)")
                .param("id", id).param("tipo", "image/" + formato).param("contenido", contenido).update();
        return RUTA + id;
    }

    public Optional<Foto> obtener(UUID id) {
        return jdbc.sql("""
                SELECT f.tipo, f.contenido FROM findimo.tt_foto_binaria f
                WHERE f.id = :id AND EXISTS (
                    SELECT 1 FROM findimo.tt_imagen_propiedad i
                    JOIN findimo.tm_propiedad p USING (idpropiedad)
                    WHERE i.rutaimagen = :ruta AND i.estado AND p.estado
                )
                """).param("id", id).param("ruta", RUTA + id)
                .query((rs, fila) -> new Foto(rs.getString("tipo"), rs.getBytes("contenido"))).optional();
    }
}
