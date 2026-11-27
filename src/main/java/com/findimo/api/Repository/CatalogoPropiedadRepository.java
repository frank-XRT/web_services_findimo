package com.findimo.api.Repository;

import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class CatalogoPropiedadRepository {
    public record Opcion(Integer id, String nombre) {}
    public record Catalogos(List<Opcion> tipos, List<Opcion> servicios) {}

    private final JdbcClient jdbc;

    public CatalogoPropiedadRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public Catalogos obtener() {
        return new Catalogos(
                jdbc.sql("""
                    select idtipopropiedad as id, nombre from findimo.tm_tipo_propiedad
                    where estado = true order by idtipopropiedad
                    """).query(Opcion.class).list(),
                jdbc.sql("""
                    select idservicio as id, nombreservicio as nombre from findimo.tm_servicio
                    where estado = true order by nombreservicio, idservicio
                    """).query(Opcion.class).list()
        );
    }
}
