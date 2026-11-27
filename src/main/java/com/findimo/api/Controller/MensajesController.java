package com.findimo.api.Controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.findimo.api.Entity.MensajeAlquilerEntity;
import com.findimo.api.Entity.SolicitudAlquilerEntity;
import com.findimo.api.Entity.SolicitudAlquilerEntity.Situacion;
import com.findimo.api.Entity.SolicitudAlquilerEntity.Reserva;
import com.findimo.api.Entity.UsuarioEntity;
import com.findimo.api.Entity.ResenaEntity;
import com.findimo.api.Repository.MensajeAlquilerRepository;
import com.findimo.api.Repository.SolicitudAlquilerRepository;
import com.findimo.api.Repository.ResenaRepository;
import com.findimo.api.Repository.PropiedadRepository;

@RestController
@RequestMapping("/api/mensajes")
public class MensajesController {
    public record Conversacion(Long idSolicitud, Long idPropiedad, String vivienda, String distrito,
            String contacto, Situacion situacion, boolean arrendador, Reserva reserva,
            Instant reservaFinalizadaEn, boolean resenaRegistrada, boolean resenaViviendaRegistrada, boolean reservaSolicitadaPorMi) {}
    public record ResenaEstudiante(Long id, Integer calificacion, String comentario, LocalDateTime fecha) {}
    public record PerfilEstudiante(String nombre, String universidad, List<ResenaEstudiante> resenas) {}
    public record DecisionReserva(Reserva estado) {}
    public record ConfirmarFinalizacion(String codigo) {}
    public record NuevaResena(Integer calificacion, String comentario) {}
    public record Mensaje(Long idMensaje, boolean propio, String texto, Instant fecha) {}
    public record TextoMensaje(String texto) {}

    private final SolicitudAlquilerRepository solicitudes;
    private final MensajeAlquilerRepository mensajes;
    private final ResenaRepository resenas;
    private final PropiedadRepository propiedades;

    public MensajesController(SolicitudAlquilerRepository solicitudes, MensajeAlquilerRepository mensajes,
            ResenaRepository resenas, PropiedadRepository propiedades) {
        this.solicitudes = solicitudes;
        this.mensajes = mensajes;
        this.resenas = resenas;
        this.propiedades = propiedades;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<Conversacion>> listar(@RequestParam("cuenta") Long cuenta, HttpServletRequest request) {
        verificarSesion(cuenta, request);
        var conversaciones = solicitudes.listarConversaciones(cuenta).stream()
                .map(solicitud -> resumir(solicitud, cuenta)).toList();
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(conversaciones);
    }

    @GetMapping("/{idSolicitud}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<Mensaje>> leer(@PathVariable("idSolicitud") Long idSolicitud,
            @RequestParam("cuenta") Long cuenta, HttpServletRequest request) {
        verificarSesion(cuenta, request);
        verificarParticipante(buscar(idSolicitud), cuenta);

        var historial = mensajes.findTop100BySolicitud_IdSolicitudAlquilerOrderByIdMensajeDesc(idSolicitud)
                .reversed().stream().map(mensaje -> convertir(mensaje, cuenta)).toList();
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(historial);
    }

    @PostMapping("/{idSolicitud}")
    @Transactional
    public Mensaje enviar(@PathVariable("idSolicitud") Long idSolicitud,
            @RequestParam("cuenta") Long cuenta, @RequestBody TextoMensaje cuerpo, HttpServletRequest request) {
        verificarSesion(cuenta, request);
        var solicitud = buscar(idSolicitud);
        var remitente = verificarParticipante(solicitud, cuenta);
        String texto = cuerpo.texto() == null ? "" : cuerpo.texto().strip();
        if (texto.isEmpty() || texto.length() > 1000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Escribe un mensaje de hasta 1000 caracteres.");
        }
        return convertir(mensajes.save(new MensajeAlquilerEntity(solicitud, remitente, texto)), cuenta);
    }

    @GetMapping("/{idSolicitud}/estudiante")
    @Transactional(readOnly = true)
    public ResponseEntity<PerfilEstudiante> perfilEstudiante(@PathVariable("idSolicitud") Long idSolicitud,
            @RequestParam("cuenta") Long cuenta, HttpServletRequest request) {
        verificarSesion(cuenta, request);
        var solicitud = buscar(idSolicitud);
        verificarArrendador(solicitud, cuenta);
        var estudiante = solicitud.getPerfilEstudiante();
        var usuario = estudiante.getUsuario();
        var recibidas = resenas.findTop10ByUsuarioObjetivo_IdUsuarioAndEstadoTrueOrderByIdResenaDesc(usuario.getIdUsuario())
                .stream().map(resena -> new ResenaEstudiante(resena.getIdResena(), resena.getCalificacion(),
                        resena.getComentario(), resena.getFechaCreacion())).toList();
        var perfil = new PerfilEstudiante(nombre(usuario), estudiante.getNombreUniversidad(), recibidas);
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(perfil);
    }

    @PutMapping("/{idSolicitud}/reserva")
    @Transactional
    public Conversacion gestionarReserva(@PathVariable("idSolicitud") Long idSolicitud,
            @RequestParam("cuenta") Long cuenta, @RequestBody DecisionReserva decision, HttpServletRequest request) {
        verificarSesion(cuenta, request);
        var solicitud = solicitudes.findByIdSolicitudAlquiler(idSolicitud)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        verificarParticipante(solicitud, cuenta);
        Reserva estado = decision.estado();
        if (estado == null || estado == Reserva.FINALIZADA) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        boolean esArrendador = cuenta.equals(arrendador(solicitud).getIdUsuario());
        boolean esSolicitante = esArrendador == solicitud.isReservaIniciadaPorArrendador();
        Reserva actual = solicitud.getReserva();
        if (solicitud.getSituacion() == Situacion.CANCELADA) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (estado == Reserva.PENDIENTE) {
            if (actual == Reserva.PENDIENTE && esSolicitante) return resumir(solicitud, cuenta);
            if (actual != null && actual != Reserva.RECHAZADA) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        } else {
            if (esSolicitante) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            if (actual == estado) return resumir(solicitud, cuenta);
            if (actual != Reserva.PENDIENTE) throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        if (estado != Reserva.RECHAZADA) {
            Long idPropiedad = solicitud.getPropiedad().getIdPropiedad();
            var vivienda = propiedades.findByIdPropiedad(idPropiedad)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            if (!Boolean.TRUE.equals(vivienda.getEstado())
                    || solicitudes.existsByPropiedad_IdPropiedadAndReserva(idPropiedad, Reserva.CONFIRMADA)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "La vivienda ya no está disponible.");
            }
        }

        if (estado == Reserva.PENDIENTE) {
            solicitud.iniciarReserva(esArrendador);
        } else {
            solicitud.setReserva(estado);
            if (estado == Reserva.CONFIRMADA) solicitud.resolver(Situacion.ACEPTADA);
        }
        return resumir(solicitud, cuenta);
    }

    @PutMapping("/{idSolicitud}/finalizar-alquiler")
    @Transactional
    public Conversacion finalizarAlquiler(@PathVariable("idSolicitud") Long idSolicitud,
            @RequestParam("cuenta") Long cuenta, @RequestBody ConfirmarFinalizacion confirmacion,
            HttpServletRequest request) {
        verificarSesion(cuenta, request);
        var solicitud = bloquearSolicitud(idSolicitud);
        verificarArrendador(solicitud, cuenta);
        if (confirmacion.codigo() == null || !("LIBERAR-" + idSolicitud).equals(confirmacion.codigo().strip())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El código de confirmación no coincide.");
        }
        if (solicitud.getReserva() == Reserva.FINALIZADA) return resumir(solicitud, cuenta);
        if (solicitud.getReserva() != Reserva.CONFIRMADA) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        solicitud.finalizarReserva();
        return resumir(solicitud, cuenta);
    }

    @PostMapping("/{idSolicitud}/resena-{destino:estudiante|arrendador|vivienda}")
    @Transactional
    public Conversacion dejarResena(@PathVariable("idSolicitud") Long idSolicitud,
            @PathVariable("destino") String destino,
            @RequestParam("cuenta") Long cuenta, @RequestBody NuevaResena datos, HttpServletRequest request) {
        verificarSesion(cuenta, request);
        var solicitud = bloquearSolicitud(idSolicitud);
        var autor = verificarParticipante(solicitud, cuenta);
        var propietario = arrendador(solicitud);
        var estudiante = solicitud.getPerfilEstudiante().getUsuario();
        boolean esArrendador = cuenta.equals(propietario.getIdUsuario());
        boolean esVivienda = destino.equals("vivienda");
        if (esArrendador != destino.equals("estudiante") || propietario.getIdUsuario().equals(estudiante.getIdUsuario())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (solicitud.getReserva() != Reserva.FINALIZADA
                || resenaRegistrada(idSolicitud, cuenta, esVivienda)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        String comentario = datos.comentario() == null ? "" : datos.comentario().strip();
        if (datos.calificacion() == null || datos.calificacion() < 1 || datos.calificacion() > 5 || comentario.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        var resena = new ResenaEntity();
        resena.setSolicitudAlquiler(solicitud);
        resena.setUsuarioAutor(autor);
        resena.setUsuarioObjetivo(esVivienda ? null : esArrendador ? estudiante : propietario);
        resena.setPropiedad(solicitud.getPropiedad());
        resena.setCalificacion(datos.calificacion());
        resena.setComentario(comentario);
        resena.setEstado(true);
        resenas.save(resena);
        return resumir(solicitud, cuenta);
    }

    private SolicitudAlquilerEntity bloquearSolicitud(Long idSolicitud) {
        return solicitudes.findByIdSolicitudAlquiler(idSolicitud)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void verificarArrendador(SolicitudAlquilerEntity solicitud, Long cuenta) {
        var propietario = arrendador(solicitud);
        if (propietario == null || !cuenta.equals(propietario.getIdUsuario())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private Conversacion resumir(SolicitudAlquilerEntity solicitud, Long cuenta) {
        var propietario = arrendador(solicitud);
        var estudiante = solicitud.getPerfilEstudiante().getUsuario();
        boolean esArrendador = cuenta.equals(propietario.getIdUsuario());
        var vivienda = solicitud.getPropiedad();
        return new Conversacion(solicitud.getIdSolicitudAlquiler(), vivienda.getIdPropiedad(), vivienda.getTitulo(),
                vivienda.getDistrito(), nombre(esArrendador ? estudiante : propietario),
                solicitud.getSituacion(), esArrendador, solicitud.getReserva(), solicitud.getReservaFinalizadaEn(),
                solicitud.getReserva() == Reserva.FINALIZADA
                        && resenaRegistrada(solicitud.getIdSolicitudAlquiler(), cuenta, false),
                !esArrendador && solicitud.getReserva() == Reserva.FINALIZADA
                        && resenaRegistrada(solicitud.getIdSolicitudAlquiler(), cuenta, true),
                solicitud.getReserva() == Reserva.PENDIENTE && esArrendador == solicitud.isReservaIniciadaPorArrendador());
    }

    private boolean resenaRegistrada(Long idSolicitud, Long idAutor, boolean esVivienda) {
        return esVivienda
                ? resenas.existsBySolicitudAlquiler_IdSolicitudAlquilerAndUsuarioAutor_IdUsuarioAndUsuarioObjetivoIsNull(idSolicitud, idAutor)
                : resenas.existsBySolicitudAlquiler_IdSolicitudAlquilerAndUsuarioAutor_IdUsuarioAndUsuarioObjetivoIsNotNull(idSolicitud, idAutor);
    }

    private String nombre(UsuarioEntity usuario) {
        return ((usuario.getNombre() == null ? "" : usuario.getNombre()) + " "
                + (usuario.getApellido() == null ? "" : usuario.getApellido())).strip();
    }

    private void verificarSesion(Long cuenta, HttpServletRequest request) {
        var sesion = request.getSession(false);
        if (sesion == null || !cuenta.equals(sesion.getAttribute(UsuarioController.USUARIO_EN_SESION))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    private SolicitudAlquilerEntity buscar(Long idSolicitud) {
        return solicitudes.findById(idSolicitud)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private UsuarioEntity verificarParticipante(SolicitudAlquilerEntity solicitud, Long cuenta) {
        var estudiante = solicitud.getPerfilEstudiante().getUsuario();
        var arrendador = arrendador(solicitud);
        if (arrendador != null) {
            if (cuenta.equals(estudiante.getIdUsuario())) return estudiante;
            if (cuenta.equals(arrendador.getIdUsuario())) return arrendador;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    private UsuarioEntity arrendador(SolicitudAlquilerEntity solicitud) {
        var perfil = solicitud.getPropiedad().getPerfilArrendador();
        return perfil == null ? null : perfil.getUsuario();
    }

    private Mensaje convertir(MensajeAlquilerEntity mensaje, Long cuenta) {
        return new Mensaje(mensaje.getIdMensaje(), cuenta.equals(mensaje.getRemitente().getIdUsuario()),
                mensaje.getTexto(), mensaje.getFecha());
    }
}
