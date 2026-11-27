package com.findimo.api;

import java.util.UUID;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=none")
@Transactional
class AdministradorTests {
    @Autowired WebApplicationContext contexto;
    @Autowired JdbcClient jdbc;
    @Autowired EntityManager entidades;
    private MockMvc mvc;

    @BeforeEach
    void preparar() {
        mvc = MockMvcBuilders.webAppContextSetup(contexto).build();
    }

    @Test
    void exigeUnaSesionAdministrativaYPermiteConsultarTodasLasSecciones() throws Exception {
        mvc.perform(get("/api/admin/resumen")).andExpect(status().isUnauthorized());
        mvc.perform(get("/auditorias")).andExpect(status().isUnauthorized());

        MockHttpSession estudiante = iniciarSesion("ESTUDIANTE");
        mvc.perform(get("/api/admin/resumen").session(estudiante)).andExpect(status().isForbidden());

        MockHttpSession administrador = iniciarSesion("ADMINISTRADOR");
        for (String seccion : new String[]{"resumen", "usuarios", "propiedades", "alquileres", "resenas", "reportes", "auditoria"}) {
            mvc.perform(get("/api/admin/" + seccion).session(administrador))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.filas").isArray());
        }

        mvc.perform(post("/api/usuarios/logout").session(administrador)).andExpect(status().isNoContent());
        mvc.perform(get("/api/admin/resumen")).andExpect(status().isUnauthorized());
    }

    @Test
    void impideRegistrarAdministradoresDesdeElFormularioPublico() throws Exception {
        mvc.perform(post("/api/usuarios/registro").contentType("application/json")
                .content("""
                        {"nombreRol":"ADMINISTRADOR"}
                        """)).andExpect(status().isBadRequest());
    }

    @Test
    void soloElAdministradorPuedeCambiarElEstadoYElLoginLoRespeta() throws Exception {
        MockHttpSession administrador = iniciarSesion("ADMINISTRADOR");
        MockHttpSession estudiante = iniciarSesion("ESTUDIANTE");
        var cuenta = crearCuenta("ESTUDIANTE");
        Long id = jdbc.sql("SELECT idusuario FROM findimo.tm_usuario WHERE correoelectronico = :correo")
                .param("correo", cuenta.correo()).query(Long.class).single();
        String ruta = "/api/admin/usuarios/" + id + "/estado";

        mvc.perform(patch(ruta).session(estudiante).contentType("application/json")
                .content("{\"activo\":false}")).andExpect(status().isForbidden());
        mvc.perform(patch(ruta).session(administrador).contentType("application/json")
                .content("{\"activo\":false}")).andExpect(status().isNoContent());
        mvc.perform(login(cuenta)).andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Tu cuenta está desactivada."));
        mvc.perform(patch(ruta).session(administrador).contentType("application/json")
                .content("{\"activo\":true}")).andExpect(status().isNoContent());
        entidades.clear();
        mvc.perform(login(cuenta)).andExpect(status().isOk());

        mvc.perform(patch("/api/admin/usuarios/" + id + "/contrasena").session(administrador)
                .contentType("application/json").content("{\"contrasena\":\"2\",\"confirmacion\":\"3\"}"))
                .andExpect(status().isBadRequest());
        cambiarContrasena(administrador, id, "2").andExpect(status().isNoContent());
        entidades.clear();
        mvc.perform(login(cuenta)).andExpect(status().isUnauthorized());
        mvc.perform(login(new Cuenta(cuenta.correo(), "2"))).andExpect(status().isOk());
        mvc.perform(get("/api/admin/auditoria").session(administrador))
                .andExpect(jsonPath("$.filas[0].evento").value("Contraseña cambiada"));
    }

    @Test
    void buscaUsuariosPorNombreCorreoOIdAntesDePaginar() throws Exception {
        MockHttpSession administrador = iniciarSesion("ADMINISTRADOR");
        var cuenta = crearCuenta("ESTUDIANTE");
        mvc.perform(get("/api/admin/usuarios").session(administrador).param("buscar", "CUENTA de pru"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.filas[0].nombre").value("Cuenta de prueba"));
        mvc.perform(get("/api/admin/usuarios").session(administrador).param("buscar", cuenta.correo().substring(0, 36)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.filas.length()").value(1))
                .andExpect(jsonPath("$.filas[0].correo").value(cuenta.correo()));
        Long id = jdbc.sql("SELECT idusuario FROM findimo.tm_usuario WHERE correoelectronico = :correo")
                .param("correo", cuenta.correo()).query(Long.class).single();
        mvc.perform(get("/api/admin/usuarios").session(administrador).param("buscar", id.toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.filas[0].id").value(id.intValue()));
    }

    @Test
    void retiraYRestauraViviendasYResenasConAuditoria() throws Exception {
        MockHttpSession administrador = iniciarSesion("ADMINISTRADOR");
        var cuenta = crearCuenta("ARRENDADOR");
        Long autor = jdbc.sql("SELECT idusuario FROM findimo.tm_usuario WHERE correoelectronico = :correo")
                .param("correo", cuenta.correo()).query(Long.class).single();
        Long perfil = jdbc.sql("""
                INSERT INTO findimo.tm_perfil_arrendador (idusuario) VALUES (:autor) RETURNING idperfilarrendador
                """).param("autor", autor).query(Long.class).single();
        Long vivienda = jdbc.sql("""
                INSERT INTO findimo.tm_propiedad (idperfilarrendador, titulo, precio, habitaciones, direccion, distrito)
                VALUES (:perfil, 'Vivienda de prueba', 500, 1, 'Calle de prueba', 'Lima') RETURNING idpropiedad
                """).param("perfil", perfil).query(Long.class).single();
        Long resena = jdbc.sql("""
                INSERT INTO findimo.tt_resena (idusuarioautor, idpropiedad, calificacion)
                VALUES (:autor, :vivienda, 5) RETURNING idresena
                """).param("autor", autor).param("vivienda", vivienda).query(Long.class).single();

        String rutaVivienda = "/api/admin/propiedades/" + vivienda + "/estado";
        String rutaResena = "/api/admin/resenas/" + resena + "/estado";
        String opiniones = "/api/resenas/propiedad/" + vivienda;
        cambiarEstado(administrador, rutaVivienda, false);
        cambiarEstado(administrador, rutaResena, false);
        mvc.perform(get("/api/admin/propiedades").session(administrador))
                .andExpect(jsonPath("$.filas[0].estado").value("Retirada"));
        mvc.perform(get(opiniones)).andExpect(jsonPath("$.length()").value(0));
        mvc.perform(get("/api/admin/auditoria").session(administrador))
                .andExpect(jsonPath("$.filas[0].evento").value("Reseña ocultada"))
                .andExpect(jsonPath("$.filas[0].cuenta").value("Cuenta de prueba"))
                .andExpect(jsonPath("$.filas[0].id_cuenta").value(administrador.getAttribute("idUsuario").toString()))
                .andExpect(jsonPath("$.filas[1].registro").value("Vivienda de prueba"));

        cambiarEstado(administrador, rutaVivienda, true);
        cambiarEstado(administrador, rutaResena, true);
        mvc.perform(get("/api/admin/propiedades").session(administrador))
                .andExpect(jsonPath("$.filas[0].estado").value("Disponible"));
        mvc.perform(get(opiniones)).andExpect(jsonPath("$[0].idResena").value(resena.intValue()));
        mvc.perform(get("/api/admin/auditoria").session(administrador))
                .andExpect(jsonPath("$.filas[0].evento").value("Reseña restaurada"));
    }

    private void cambiarEstado(MockHttpSession administrador, String ruta, boolean activo) throws Exception {
        mvc.perform(patch(ruta).session(administrador).contentType("application/json")
                .content("{\"activo\":" + activo + "}")).andExpect(status().isNoContent());
    }

    @Test
    void presentaCadaReporteConSusIndicadores() throws Exception {
        MockHttpSession administrador = iniciarSesion("ADMINISTRADOR");
        String ruta = "/api/admin/reportes";
        mvc.perform(get(ruta).session(administrador).param("buscar", "solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filas[0].grupo").value("Pendientes de respuesta"));
        mvc.perform(get(ruta).session(administrador).param("buscar", "reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filas[0].grupo").value("Pendientes de confirmación"));
        mvc.perform(get(ruta).session(administrador).param("buscar", "viviendas"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.filas").isArray());
        mvc.perform(get(ruta).session(administrador).param("buscar", "resenas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filas[0].grupo").value("Sobre viviendas"))
                .andExpect(jsonPath("$.filas[0].ocultas").isNumber());
    }

    @Test
    void soloLaCuentaSupremaGestionaAdministradoresYEstaProtegida() throws Exception {
        String correoSupremo = "adminsupremo@upc.edu.pe";
        jdbc.sql("""
                INSERT INTO findimo.tm_usuario (idrol, correoelectronico, contrasena, nombre, apellido)
                SELECT idrol, :correo, :clave, 'Admin', 'Supremo'
                FROM findimo.tm_rol WHERE nombrerol = 'ADMINISTRADOR'
                AND NOT EXISTS (SELECT 1 FROM findimo.tm_usuario WHERE correoelectronico = :correo)
                """).param("correo", correoSupremo).param("clave", UUID.randomUUID().toString()).update();
        Long idSupremo = jdbc.sql("SELECT idusuario FROM findimo.tm_usuario WHERE correoelectronico = :correo")
                .param("correo", correoSupremo).query(Long.class).single();
        var supremo = new MockHttpSession();
        supremo.setAttribute("idUsuario", idSupremo);
        MockHttpSession secundario = iniciarSesion("ADMINISTRADOR");
        mvc.perform(patch("/api/admin/usuarios/" + idSupremo + "/estado").session(secundario)
                .contentType("application/json").content("{\"activo\":false}")).andExpect(status().isForbidden());
        verificarAltaYBajaDeSecundario(supremo, secundario, idSupremo);
    }

    private void verificarAltaYBajaDeSecundario(MockHttpSession supremo, MockHttpSession secundario, Long idSupremo)
            throws Exception {
        String ruta = "/api/admin/administradores";
        var cuenta = new Cuenta(UUID.randomUUID() + "@example.test", "1");
        String cuerpo = """
                {"correo":"%s","contrasena":"%s","confirmacion":"%s"}
                """;
        String valido = cuerpo.formatted(cuenta.correo(), cuenta.contrasena(), cuenta.contrasena());
        mvc.perform(get(ruta).session(secundario)).andExpect(status().isForbidden());
        mvc.perform(post(ruta).session(secundario).contentType("application/json").content(valido))
                .andExpect(status().isForbidden());
        mvc.perform(post(ruta).session(supremo).contentType("application/json")
                .content(cuerpo.formatted(cuenta.correo(), cuenta.contrasena(), "otra-clave")))
                .andExpect(status().isBadRequest());
        mvc.perform(post(ruta).session(supremo).contentType("application/json").content(valido))
                .andExpect(status().isCreated());
        mvc.perform(post(ruta).session(supremo).contentType("application/json").content(valido))
                .andExpect(status().isConflict());
        mvc.perform(get(ruta).session(supremo).param("buscar", cuenta.correo()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.filas[0].correo").value(cuenta.correo()));
        mvc.perform(get("/api/admin/auditoria").session(supremo))
                .andExpect(jsonPath("$.filas[0].evento").value("Administrador secundario creado"));
        mvc.perform(login(cuenta))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nombreRol").value("ADMINISTRADOR"));

        Long id = jdbc.sql("SELECT idusuario FROM findimo.tm_usuario WHERE correoelectronico = :correo")
                .param("correo", cuenta.correo()).query(Long.class).single();
        String estado = "/api/admin/usuarios/" + id + "/estado";
        mvc.perform(patch(estado).session(secundario).contentType("application/json").content("{\"activo\":false}"))
                .andExpect(status().isForbidden());
        mvc.perform(patch("/api/admin/usuarios/" + idSupremo + "/estado").session(supremo)
                .contentType("application/json").content("{\"activo\":false}")).andExpect(status().isConflict());
        cambiarContrasena(secundario, id, "3").andExpect(status().isForbidden());
        cambiarContrasena(secundario, idSupremo, "3").andExpect(status().isForbidden());
        cambiarContrasena(supremo, idSupremo, "4").andExpect(status().isNoContent());
        entidades.clear();
        mvc.perform(login(new Cuenta("adminsupremo@upc.edu.pe", "4")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nombreRol").value("ADMINISTRADOR"));
        cambiarContrasena(supremo, id, "3").andExpect(status().isNoContent());
        entidades.clear();
        var cuentaActualizada = new Cuenta(cuenta.correo(), "3");
        mvc.perform(login(cuentaActualizada)).andExpect(status().isOk());
        cambiarEstado(supremo, estado, false);
        entidades.clear();
        mvc.perform(login(cuentaActualizada)).andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Tu cuenta está desactivada."));
    }

    private org.springframework.test.web.servlet.ResultActions cambiarContrasena(
            MockHttpSession sesion, Long id, String clave) throws Exception {
        return mvc.perform(patch("/api/admin/usuarios/" + id + "/contrasena").session(sesion)
                .contentType("application/json")
                .content("""
                        {"contrasena":"%s","confirmacion":"%s"}
                        """.formatted(clave, clave)));
    }

    @Test
    void elUsuarioActualizaSuPropiaContrasenaDesdeLaSesion() throws Exception {
        var cuenta = crearCuenta("ESTUDIANTE");
        String ruta = "/api/usuarios/contrasena";
        String datos = "{\"contrasena\":\"7\"}";
        mvc.perform(patch(ruta).contentType("application/json").content(datos))
                .andExpect(status().isUnauthorized());

        var login = mvc.perform(login(cuenta)).andExpect(status().isOk()).andReturn();
        var sesion = (MockHttpSession) login.getRequest().getSession(false);
        mvc.perform(patch(ruta).session(sesion).contentType("application/json").content(datos))
                .andExpect(status().isNoContent());

        entidades.clear();
        mvc.perform(login(cuenta)).andExpect(status().isUnauthorized());
        mvc.perform(login(new Cuenta(cuenta.correo(), "7"))).andExpect(status().isOk());
    }

    @Test
    void elRegistroDeAmbosTiposGuardaUnHashYPermiteIngresar() throws Exception {
        for (String rol : new String[]{"ESTUDIANTE", "ARRENDADOR"}) {
            var cuenta = new Cuenta(UUID.randomUUID() + "@example.test", "clave-prueba-123");
            mvc.perform(post("/api/usuarios/registro").contentType("application/json").content("""
                    {"correoElectronico":"%s","contrasena":"%s","nombreRol":"%s","nombre":"Cuenta",
                     "apellido":"de prueba","nombreUniversidad":"UPC","cuentaBancaria":"123456789"}
                    """.formatted(cuenta.correo(), cuenta.contrasena(), rol)))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.nombreRol").value(rol));
            verificarHashGuardado(cuenta);
            mvc.perform(login(cuenta)).andExpect(status().isOk());
        }
    }

    @Test
    void elPrimerLoginActualizaLaContrasenaLegada() throws Exception {
        var cuenta = crearCuenta("ESTUDIANTE");
        mvc.perform(login(cuenta)).andExpect(status().isOk());
        verificarHashGuardado(cuenta);
        entidades.clear();
        mvc.perform(login(cuenta)).andExpect(status().isOk());
    }

    private void verificarHashGuardado(Cuenta cuenta) {
        String guardada = jdbc.sql("SELECT contrasena FROM findimo.tm_usuario WHERE correoelectronico = :correo")
                .param("correo", cuenta.correo()).query(String.class).single();
        org.junit.jupiter.api.Assertions.assertTrue(guardada.startsWith("{bcrypt}"));
        org.junit.jupiter.api.Assertions.assertNotEquals(cuenta.contrasena(), guardada);
    }

    @Test
    void ambasPartesPuedenSolicitarAlquilerSinAceptacionPrevia() throws Exception {
        MockHttpSession estudiante = iniciarSesion("ESTUDIANTE");
        MockHttpSession arrendador = iniciarSesion("ARRENDADOR");
        Long perfilEstudiante = jdbc.sql("""
                INSERT INTO findimo.tt_perfil_estudiante (idusuario, nombreuniversidad)
                VALUES (:id, 'Universidad de prueba') RETURNING idperfilestudiante
                """).param("id", estudiante.getAttribute("idUsuario")).query(Long.class).single();
        Long perfilArrendador = jdbc.sql("""
                INSERT INTO findimo.tm_perfil_arrendador (idusuario) VALUES (:id) RETURNING idperfilarrendador
                """).param("id", arrendador.getAttribute("idUsuario")).query(Long.class).single();
        Long vivienda = jdbc.sql("""
                INSERT INTO findimo.tm_propiedad (idperfilarrendador, titulo, precio, habitaciones, direccion, distrito)
                VALUES (:perfil, 'Vivienda de prueba', 500, 1, 'Calle de prueba', 'Lima') RETURNING idpropiedad
                """).param("perfil", perfilArrendador).query(Long.class).single();
        Long solicitud = jdbc.sql("""
                INSERT INTO findimo.tt_solicitud_alquiler (idperfilestudiante, idpropiedad, estado)
                VALUES (:estudiante, :propiedad, 'PENDIENTE') RETURNING idsolicitudalquiler
                """).param("estudiante", perfilEstudiante).param("propiedad", vivienda).query(Long.class).single();

        reserva(estudiante, solicitud, "PENDIENTE").andExpect(status().isOk())
                .andExpect(jsonPath("$.reservaSolicitadaPorMi").value(true));
        reserva(estudiante, solicitud, "CONFIRMADA").andExpect(status().isForbidden());
        reserva(arrendador, solicitud, "RECHAZADA").andExpect(status().isOk());
        reserva(arrendador, solicitud, "PENDIENTE").andExpect(status().isOk())
                .andExpect(jsonPath("$.reservaSolicitadaPorMi").value(true));
        reserva(arrendador, solicitud, "CONFIRMADA").andExpect(status().isForbidden());
        reserva(estudiante, solicitud, "CONFIRMADA").andExpect(status().isOk())
                .andExpect(jsonPath("$.reserva").value("CONFIRMADA"));
    }

    private org.springframework.test.web.servlet.ResultActions reserva(MockHttpSession sesion, Long id, String estado)
            throws Exception {
        return mvc.perform(put("/api/mensajes/" + id + "/reserva").session(sesion)
                .param("cuenta", sesion.getAttribute("idUsuario").toString()).contentType("application/json")
                .content("{\"estado\":\"" + estado + "\"}"));
    }

    @Test
    void guardaLugaresYServiciosPersonalizadosAlEditarUnaVivienda() throws Exception {
        MockHttpSession arrendador = iniciarSesion("ARRENDADOR");
        Long perfil = jdbc.sql("""
                INSERT INTO findimo.tm_perfil_arrendador (idusuario) VALUES (:id) RETURNING idperfilarrendador
                """).param("id", arrendador.getAttribute("idUsuario")).query(Long.class).single();
        Long vivienda = jdbc.sql("""
                INSERT INTO findimo.tm_propiedad (idperfilarrendador, titulo, precio, habitaciones, direccion, distrito, entorno)
                VALUES (:perfil, 'Vivienda de prueba', 500, 1, 'Calle de prueba', 'Lima',
                    '[{"tipo":"UNIVERSIDAD","nombre":"UPC","minutos":8}]'::jsonb) RETURNING idpropiedad
                """).param("perfil", perfil).query(Long.class).single();
        jdbc.sql("""
                INSERT INTO findimo.tt_imagen_propiedad (idpropiedad, rutaimagen)
                VALUES (:id, '/imagenes/prueba.jpg')
                """).param("id", vivienda).update();

        String datos = """
                {"titulo":"Vivienda de prueba","descripcion":"Descripción","precio":500,"habitaciones":1,
                 "area":25,"direccion":"Calle de prueba","distrito":"Lima","estado":true,
                 "entorno":[{"tipo":"UNIVERSIDAD","nombre":"UPC","minutos":8},
                   {"tipo":"PERSONALIZADO","categoria":"Gimnasio","nombre":"Smart Fit","minutos":6},
                   {"tipo":"PERSONALIZADO","categoria":"Cine","nombre":"Cine de prueba","minutos":12}],
                 "serviciosPersonalizados":["Bicicletero"]}
                """;
        var parte = new MockMultipartFile("propiedad", "", "application/json", datos.getBytes(StandardCharsets.UTF_8));
        mvc.perform(multipart(HttpMethod.PUT, "/api/propiedades/{id}", vivienda).file(parte).session(arrendador))
                .andExpect(status().isOk()).andExpect(jsonPath("$.entorno[1].categoria").value("Gimnasio"))
                .andExpect(jsonPath("$.entorno[2].categoria").value("Cine"))
                .andExpect(jsonPath("$.serviciosPersonalizados[0]").value("Bicicletero"));

        entidades.flush();
        entidades.clear();
        String filtro = "$[?(@.idPropiedad == " + vivienda + ")]";
        mvc.perform(get("/api/propiedades"))
                .andExpect(status().isOk()).andExpect(jsonPath(filtro + ".entorno[1].minutos").value(org.hamcrest.Matchers.contains(6)))
                .andExpect(jsonPath(filtro + ".serviciosPersonalizados[0]").value(
                    org.hamcrest.Matchers.contains("Bicicletero")));
    }

    private record Cuenta(String correo, String contrasena) {}

    private MockHttpSession iniciarSesion(String rol) throws Exception {
        var resultado = mvc.perform(login(crearCuenta(rol)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nombreRol").value(rol)).andReturn();
        return (MockHttpSession) resultado.getRequest().getSession(false);
    }

    private Cuenta crearCuenta(String rol) {
        String correo = UUID.randomUUID() + "@example.test";
        String contrasena = UUID.randomUUID().toString();
        int filas = jdbc.sql("""
                INSERT INTO findimo.tm_usuario (idrol, correoelectronico, contrasena, nombre, apellido)
                SELECT idrol, :correo, :contrasena, 'Cuenta', 'de prueba'
                FROM findimo.tm_rol WHERE nombrerol = :rol
                """).param("correo", correo).param("contrasena", contrasena).param("rol", rol).update();
        org.junit.jupiter.api.Assertions.assertEquals(1, filas, "Debe existir el rol " + rol);

        return new Cuenta(correo, contrasena);
    }

    private org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder login(Cuenta cuenta) {
        return post("/api/usuarios/login").contentType("application/json")
                .content("""
                        {"correoElectronico":"%s","contrasena":"%s"}
                        """.formatted(cuenta.correo(), cuenta.contrasena()));
    }
}
