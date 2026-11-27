package com.findimo.api.Config;

import static com.findimo.api.Service.AdministradorService.CORREO_SUPREMO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("cloud")
public class AdminInicial implements ApplicationRunner {
    private final JdbcClient jdbc;
    private final PasswordEncoder claves;
    private final String claveInicial;

    public AdminInicial(JdbcClient jdbc, PasswordEncoder claves,
            @Value("${FINDIMO_ADMIN_INITIAL_PASSWORD:}") String claveInicial) {
        this.jdbc = jdbc;
        this.claves = claves;
        this.claveInicial = claveInicial;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        jdbc.sql("SELECT 1 FROM pg_advisory_xact_lock(90123123)").query(Integer.class).single();
        jdbc.sql("""
                INSERT INTO findimo.tm_rol (nombrerol)
                SELECT nombre FROM (VALUES ('ESTUDIANTE'), ('ARRENDADOR'), ('ADMINISTRADOR')) AS roles(nombre)
                WHERE NOT EXISTS (SELECT 1 FROM findimo.tm_rol WHERE nombrerol = roles.nombre)
                """).update();
        boolean existe = jdbc.sql("""
                SELECT EXISTS (SELECT 1 FROM findimo.tm_usuario
                    WHERE lower(trim(correoelectronico)) = :correo)
                """).param("correo", CORREO_SUPREMO).query(Boolean.class).single();
        if (existe) return;
        if (claveInicial.isEmpty()) throw new IllegalStateException("Configura FINDIMO_ADMIN_INITIAL_PASSWORD.");

        jdbc.sql("""
                INSERT INTO findimo.tm_usuario (idrol, correoelectronico, contrasena, nombre, apellido)
                SELECT idrol, :correo, :clave, 'Admin', 'Supremo'
                FROM findimo.tm_rol WHERE nombrerol = 'ADMINISTRADOR'
                """).param("correo", CORREO_SUPREMO).param("clave", claves.encode(claveInicial)).update();
    }
}
