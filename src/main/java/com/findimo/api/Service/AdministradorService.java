package com.findimo.api.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdministradorService {
    public static final String CORREO_SUPREMO = "adminsupremo@upc.edu.pe";
    private static final int TAMANO_PAGINA = 25;
    private static final Pattern CORREO_VALIDO = Pattern.compile("[^\\s@]+@[^\\s@]+\\.[^\\s@]+");
    private final JdbcClient jdbc;
    private final PasswordEncoder claves;

    public AdministradorService(JdbcClient jdbc, PasswordEncoder claves) {
        this.jdbc = jdbc;
        this.claves = claves;
    }

    public void verificarCuenta(Long cuenta) {
        if (cuenta == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        boolean administrador = jdbc.sql("""
                SELECT EXISTS (
                    SELECT 1 FROM findimo.tm_usuario u
                    JOIN findimo.tm_rol r USING (idrol)
                    WHERE u.idusuario = :cuenta AND u.estado AND r.nombrerol = 'ADMINISTRADOR'
                )
                """).param("cuenta", cuenta).query(Boolean.class).single();

        if (!administrador) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    private record CuentaAdministrada(boolean activa, String rol, boolean suprema) {}

    private CuentaAdministrada bloquearCuenta(Long id) {
        return jdbc.sql("""
                SELECT u.estado, r.nombrerol, lower(trim(u.correoelectronico)) = :supremo
                FROM findimo.tm_usuario u JOIN findimo.tm_rol r USING (idrol)
                WHERE u.idusuario = :id FOR UPDATE OF u
                """).param("supremo", CORREO_SUPREMO).param("id", id)
                .query((rs, fila) -> new CuentaAdministrada(rs.getBoolean(1), rs.getString(2), rs.getBoolean(3)))
                .optional().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private boolean esSupremo(Long id) {
        return jdbc.sql("""
                SELECT EXISTS (
                    SELECT 1 FROM findimo.tm_usuario u JOIN findimo.tm_rol r USING (idrol)
                    WHERE u.idusuario = :id AND u.estado AND r.nombrerol = 'ADMINISTRADOR'
                      AND lower(trim(u.correoelectronico)) = :supremo
                )
                """).param("id", id).param("supremo", CORREO_SUPREMO).query(Boolean.class).single();
    }

    public void verificarSupremo(Long cuenta) {
        verificarCuenta(cuenta);
        if (!esSupremo(cuenta)) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    public void reservarCorreoDisponible(String correo) {
        if (correo == null || correo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingresa un correo.");
        }
        jdbc.sql("SELECT 1 FROM pg_advisory_xact_lock(hashtextextended(lower(trim(:correo)), 0))")
                .param("correo", correo).query(Integer.class).single();
        boolean existe = jdbc.sql("""
                SELECT EXISTS (SELECT 1 FROM findimo.tm_usuario
                    WHERE lower(trim(correoelectronico)) = lower(trim(:correo)))
                """).param("correo", correo).query(Boolean.class).single();
        if (existe) throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado.");
    }

    @Transactional
    public void crearSecundario(Long administrador, String correo, String contrasena, String confirmacion) {
        verificarSupremo(administrador);
        String email = correo == null ? "" : correo.strip();
        if (email.length() > 255 || !CORREO_VALIDO.matcher(email).matches() || CORREO_SUPREMO.equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingresa un correo válido para la cuenta secundaria.");
        }
        validarContrasena(contrasena, confirmacion);

        reservarCorreoDisponible(email);
        Long id = jdbc.sql("""
                INSERT INTO findimo.tm_usuario (idrol, correoelectronico, contrasena, nombre, apellido)
                SELECT idrol, :correo, :contrasena, :nombre, ''
                FROM findimo.tm_rol WHERE nombrerol = 'ADMINISTRADOR'
                RETURNING idusuario
                """).param("correo", email).param("contrasena", claves.encode(contrasena))
                .param("nombre", email.substring(0, email.indexOf('@'))).query(Long.class).single();
        jdbc.sql("""
                INSERT INTO findimo.tt_auditoria (idusuarioeditar, datosnuevos)
                VALUES (:administrador, jsonb_build_object('rol', 'ADMINISTRADOR', 'accion', 'crear_secundario',
                    'seccion', 'usuarios', 'idRegistro', CAST(:id AS bigint)))
                """).param("administrador", administrador).param("id", id).update();
    }

    private void validarContrasena(String contrasena, String confirmacion) {
        if (contrasena == null || contrasena.isEmpty() || contrasena.getBytes(StandardCharsets.UTF_8).length > 72
                || !contrasena.equals(confirmacion)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingresa una contraseña de hasta 72 bytes y confírmala exactamente.");
        }
    }

    @Transactional
    public void cambiarContrasena(Long administrador, Long id, String contrasena, String confirmacion) {
        verificarCuenta(administrador);
        var destino = bloquearCuenta(id);
        boolean permitido = destino.suprema()
                ? administrador.equals(id)
                : !administrador.equals(id) && (!"ADMINISTRADOR".equals(destino.rol()) || esSupremo(administrador));
        if (!permitido) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        validarContrasena(contrasena, confirmacion);
        jdbc.sql("UPDATE findimo.tm_usuario SET contrasena = :clave WHERE idusuario = :id")
                .param("clave", claves.encode(contrasena)).param("id", id).update();
        jdbc.sql("""
                INSERT INTO findimo.tt_auditoria (idusuarioeditar, datosnuevos)
                VALUES (:administrador, jsonb_build_object('accion', 'cambiar_contrasena',
                    'seccion', 'usuarios', 'idRegistro', CAST(:id AS bigint)))
                """).param("administrador", administrador).param("id", id).update();
    }

    @Transactional
    public void cambiarEstado(Long administrador, String seccion, Long id, boolean activo) {
        boolean anterior = switch (seccion) {
            case "usuarios" -> {
                if (administrador.equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "No puedes cambiar el estado de tu propia cuenta.");
                }
                var destino = bloquearCuenta(id);
                if (destino.suprema() || ("ADMINISTRADOR".equals(destino.rol()) && !esSupremo(administrador))) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                }
                yield destino.activa();
            }
            case "propiedades" -> bloquearEstado(
                    "SELECT estado FROM findimo.tm_propiedad WHERE idpropiedad = :id FOR UPDATE", id);
            case "resenas" -> bloquearEstado("SELECT estado FROM findimo.tt_resena WHERE idresena = :id FOR UPDATE", id);
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        };
        if (anterior == activo) return;

        String actualizar = switch (seccion) {
            case "usuarios" -> "UPDATE findimo.tm_usuario SET estado = :activo WHERE idusuario = :id";
            case "propiedades" -> "UPDATE findimo.tm_propiedad SET estado = :activo WHERE idpropiedad = :id";
            case "resenas" -> "UPDATE findimo.tt_resena SET estado = :activo WHERE idresena = :id";
            default -> throw new IllegalStateException("Sección no válida");
        };
        jdbc.sql(actualizar).param("activo", activo).param("id", id).update();
        jdbc.sql("""
                INSERT INTO findimo.tt_auditoria (idusuarioeditar, datosanteriores, datosnuevos)
                VALUES (:administrador, jsonb_build_object('activo', CAST(:anterior AS boolean)),
                    jsonb_build_object('activo', CAST(:activo AS boolean),
                        'seccion', CAST(:seccion AS text), 'idRegistro', CAST(:id AS bigint)))
                """).param("administrador", administrador).param("anterior", anterior)
                .param("activo", activo).param("seccion", seccion).param("id", id).update();
    }

    private boolean bloquearEstado(String consulta, Long id) {
        return jdbc.sql(consulta).param("id", id).query(Boolean.class).optional()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private String consultaReporte(String reporte) {
        return switch (reporte) {
            case "", "solicitudes" -> """
                    SELECT etapas.grupo, count(s.idsolicitudalquiler) AS total
                    FROM (VALUES (1, 'PENDIENTE', 'Pendientes de respuesta'),
                                 (2, 'ACEPTADA', 'Aceptadas'),
                                 (3, 'CANCELADA', 'Canceladas')) AS etapas(orden, codigo, grupo)
                    LEFT JOIN findimo.tt_solicitud_alquiler s ON s.estado = etapas.codigo
                    GROUP BY etapas.orden, etapas.grupo ORDER BY etapas.orden
                    """;
            case "reservas" -> """
                    SELECT etapas.grupo, count(s.idsolicitudalquiler) AS total
                    FROM (VALUES (1, 'PENDIENTE', 'Pendientes de confirmación'),
                                 (2, 'CONFIRMADA', 'Confirmadas'),
                                 (3, 'FINALIZADA', 'Finalizadas'),
                                 (4, 'RECHAZADA', 'Rechazadas')) AS etapas(orden, codigo, grupo)
                    LEFT JOIN findimo.tt_solicitud_alquiler s ON s.estado_reserva = etapas.codigo
                    GROUP BY etapas.orden, etapas.grupo ORDER BY etapas.orden
                    """;
            case "viviendas" -> """
                    WITH reservadas AS (
                        SELECT DISTINCT idpropiedad FROM findimo.tt_solicitud_alquiler
                        WHERE estado_reserva = 'CONFIRMADA'
                    )
                    SELECT p.distrito AS grupo,
                           count(*) FILTER (WHERE p.estado AND r.idpropiedad IS NULL) AS disponibles,
                           count(*) FILTER (WHERE p.estado AND r.idpropiedad IS NOT NULL) AS reservadas,
                           count(*) FILTER (WHERE NOT p.estado) AS retiradas,
                           count(*) AS total
                    FROM findimo.tm_propiedad p LEFT JOIN reservadas r USING (idpropiedad)
                    GROUP BY p.distrito ORDER BY disponibles DESC, p.distrito
                    """;
            case "resenas" -> """
                    SELECT tipos.grupo, count(r.idresena) FILTER (WHERE r.estado) AS visibles,
                           count(r.idresena) FILTER (WHERE NOT r.estado) AS ocultas,
                           count(r.idresena) AS total
                    FROM (VALUES (true, 'Sobre viviendas'), (false, 'Sobre personas')) AS tipos(vivienda, grupo)
                    LEFT JOIN findimo.tt_resena r ON r.idresena IS NOT NULL
                        AND (r.idusuarioobjetivo IS NULL) = tipos.vivienda
                    GROUP BY tipos.grupo, tipos.vivienda ORDER BY tipos.vivienda DESC
                    """;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        };
    }

    public record Pagina(List<Map<String, Object>> filas, boolean haySiguiente) {}

    public Pagina consultar(String seccion, int pagina, String buscar) {
        if (pagina < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        String consulta = switch (seccion) {
            case "resumen" -> """
                    SELECT 'Estudiantes' AS indicador, count(*) AS total FROM findimo.tt_perfil_estudiante
                    UNION ALL SELECT 'Arrendadores', count(*) FROM findimo.tm_perfil_arrendador
                    UNION ALL SELECT 'Propiedades', count(*) FROM findimo.tm_propiedad
                    UNION ALL SELECT 'Solicitudes de alquiler', count(*) FROM findimo.tt_solicitud_alquiler
                    """;
            case "usuarios", "administradores" -> """
                    SELECT u.idusuario AS id, concat_ws(' ', u.nombre, u.apellido) AS nombre,
                           u.correoelectronico AS correo, r.nombrerol AS rol,
                           CASE WHEN u.estado THEN 'Activa' ELSE 'Inactiva' END AS estado
                    FROM findimo.tm_usuario u JOIN findimo.tm_rol r USING (idrol)
                    WHERE (NOT :soloAdministradores OR r.nombrerol = 'ADMINISTRADOR')
                      AND (strpos(lower(concat_ws(' ', u.nombre, u.apellido)), lower(:buscar)) > 0
                        OR strpos(lower(u.correoelectronico), lower(:buscar)) > 0
                        OR u.idusuario::text = :buscar)
                    ORDER BY (u.idusuario::text = :buscar) DESC, u.idusuario DESC
                    """;
            case "propiedades" -> """
                    SELECT p.idpropiedad AS id, p.titulo, p.distrito, p.precio,
                           CASE WHEN NOT p.estado THEN 'Retirada'
                               WHEN EXISTS (SELECT 1 FROM findimo.tt_solicitud_alquiler s
                               WHERE s.idpropiedad = p.idpropiedad AND s.estado_reserva = 'CONFIRMADA')
                               THEN 'Reservada' ELSE 'Disponible' END AS estado
                    FROM findimo.tm_propiedad p ORDER BY p.idpropiedad DESC
                    """;
            case "alquileres" -> """
                    SELECT s.idsolicitudalquiler AS id, p.titulo,
                           concat_ws(' ', u.nombre, u.apellido) AS estudiante, s.estado,
                           coalesce(s.estado_reserva, '—') AS reserva, s.fechacreacion AS fecha
                    FROM findimo.tt_solicitud_alquiler s JOIN findimo.tm_propiedad p USING (idpropiedad)
                    JOIN findimo.tt_perfil_estudiante e USING (idperfilestudiante)
                    JOIN findimo.tm_usuario u ON u.idusuario = e.idusuario
                    ORDER BY s.idsolicitudalquiler DESC
                    """;
            case "resenas" -> """
                    SELECT r.idresena AS id, concat_ws(' ', u.nombre, u.apellido) AS autor,
                           coalesce(nullif(concat_ws(' ', d.nombre, d.apellido), ''), p.titulo) AS destino,
                           r.calificacion, r.comentario,
                           CASE WHEN r.estado THEN 'Visible' ELSE 'Oculta' END AS estado
                    FROM findimo.tt_resena r JOIN findimo.tm_usuario u ON u.idusuario = r.idusuarioautor
                    LEFT JOIN findimo.tm_usuario d ON d.idusuario = r.idusuarioobjetivo
                    LEFT JOIN findimo.tm_propiedad p USING (idpropiedad)
                    ORDER BY r.idresena DESC
                    """;
            case "reportes" -> consultaReporte(buscar);
            case "auditoria" -> """
                    SELECT idauditoria AS id, fecharegistro AS fecha,
                           CASE WHEN datosnuevos->>'accion' = 'cambiar_contrasena' THEN 'Contraseña cambiada'
                                WHEN datosnuevos->>'accion' = 'crear_secundario' THEN 'Administrador secundario creado'
                                WHEN datosnuevos->>'accion' = 'nombrar_secundario' THEN 'Administrador secundario nombrado'
                                WHEN datosnuevos->>'seccion' = 'usuarios' THEN
                                    CASE datosnuevos->>'activo' WHEN 'true' THEN 'Cuenta reactivada' ELSE 'Cuenta desactivada' END
                                WHEN datosnuevos->>'seccion' = 'propiedades' THEN
                                    CASE datosnuevos->>'activo' WHEN 'true' THEN 'Vivienda restaurada' ELSE 'Vivienda retirada' END
                                WHEN datosnuevos->>'seccion' = 'resenas' THEN
                                    CASE datosnuevos->>'activo' WHEN 'true' THEN 'Reseña restaurada' ELSE 'Reseña ocultada' END
                                WHEN idusuarioeliminar IS NOT NULL THEN 'Eliminación'
                                WHEN idusuarioeditar IS NOT NULL THEN 'Edición'
                                WHEN jsonb_exists(datosnuevos, 'nombreRol') THEN 'Registro de cuenta'
                                ELSE 'Evento registrado' END AS evento,
                           nullif(trim(concat_ws(' ', cuenta.nombre, cuenta.apellido)), '') AS cuenta,
                           coalesce(idusuarioeditar::text, idusuarioeliminar::text, datosnuevos->>'idUsuario') AS id_cuenta,
                           CASE datosnuevos->>'seccion'
                               WHEN 'usuarios' THEN coalesce(nullif(concat_ws(' ', usuario.nombre, usuario.apellido), ''),
                                   'Usuario #' || (datosnuevos->>'idRegistro'))
                               WHEN 'propiedades' THEN coalesce(vivienda.titulo, 'Vivienda #' || (datosnuevos->>'idRegistro'))
                               WHEN 'resenas' THEN 'Reseña #' || (datosnuevos->>'idRegistro')
                           END AS registro,
                           CASE WHEN datosnuevos->>'accion' = 'cambiar_contrasena' THEN NULL
                               WHEN datosnuevos->>'accion' = 'crear_secundario' THEN 'Cuenta activa creada'
                               WHEN datosnuevos->>'accion' = 'nombrar_secundario' THEN (datosanteriores->>'rol') || ' → Administrador secundario'
                               ELSE CASE datosnuevos->>'seccion'
                               WHEN 'usuarios' THEN CASE datosanteriores->>'activo' WHEN 'true' THEN 'Activa → Inactiva' ELSE 'Inactiva → Activa' END
                               WHEN 'propiedades' THEN CASE datosanteriores->>'activo' WHEN 'true' THEN 'Publicada → Retirada' ELSE 'Retirada → Publicada' END
                               WHEN 'resenas' THEN CASE datosanteriores->>'activo' WHEN 'true' THEN 'Visible → Oculta' ELSE 'Oculta → Visible' END
                           END END AS cambio
                    FROM findimo.tt_auditoria a
                    LEFT JOIN findimo.tm_usuario cuenta ON cuenta.idusuario::text =
                        coalesce(a.idusuarioeditar::text, a.idusuarioeliminar::text, a.datosnuevos->>'idUsuario')
                    LEFT JOIN findimo.tm_usuario usuario ON a.datosnuevos->>'seccion' = 'usuarios'
                        AND usuario.idusuario::text = a.datosnuevos->>'idRegistro'
                    LEFT JOIN findimo.tm_propiedad vivienda ON a.datosnuevos->>'seccion' = 'propiedades'
                        AND vivienda.idpropiedad::text = a.datosnuevos->>'idRegistro'
                    ORDER BY idauditoria DESC
                    """;
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        };

        var consultaPaginada = jdbc.sql(consulta + " LIMIT :limite OFFSET :desplazamiento")
                .param("limite", TAMANO_PAGINA + 1)
                .param("desplazamiento", (long) pagina * TAMANO_PAGINA);
        if ("usuarios".equals(seccion) || "administradores".equals(seccion)) {
            consultaPaginada.param("buscar", buscar.strip()).param("soloAdministradores", "administradores".equals(seccion));
        }
        var filas = consultaPaginada.query().listOfRows();

        boolean haySiguiente = filas.size() > TAMANO_PAGINA;
        return new Pagina(haySiguiente ? filas.subList(0, TAMANO_PAGINA) : filas, haySiguiente);
    }
}
