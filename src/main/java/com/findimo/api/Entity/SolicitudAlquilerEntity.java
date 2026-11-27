package com.findimo.api.Entity;

import java.time.LocalDateTime;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tt_solicitud_alquiler", schema = "findimo")
public class SolicitudAlquilerEntity {
    public enum Situacion {
        PENDIENTE, ACEPTADA, CANCELADA
    }

    public enum Reserva { PENDIENTE, CONFIRMADA, RECHAZADA, FINALIZADA }

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_reserva", length = 12)
    private Reserva reserva;

    @Column(name = "reserva_iniciada_por_arrendador", nullable = false)
    private boolean reservaIniciadaPorArrendador;

    public boolean isReservaIniciadaPorArrendador() {
        return reservaIniciadaPorArrendador;
    }

    public void iniciarReserva(boolean arrendador) {
        reserva = Reserva.PENDIENTE;
        reservaIniciadaPorArrendador = arrendador;
    }

    @Column(name = "reserva_finalizada_en")
    private Instant reservaFinalizadaEn;

    public Instant getReservaFinalizadaEn() {
        return reservaFinalizadaEn;
    }

    public void finalizarReserva() {
        reserva = Reserva.FINALIZADA;
        reservaFinalizadaEn = Instant.now();
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDSOLICITUDALQUILER")
    private Long idSolicitudAlquiler;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDPERFILESTUDIANTE", nullable = false)
    private PerfilEstudianteEntity perfilEstudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDPROPIEDAD", nullable = false)
    private PropiedadEntity propiedad;

    @Column(name = "ESTADO", nullable = false)
    private String estado = Situacion.PENDIENTE.name();

    @Column(name = "FECHACREACION", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public SolicitudAlquilerEntity() {
    }

    public Long getIdSolicitudAlquiler() {
        return idSolicitudAlquiler;
    }

    public void setIdSolicitudAlquiler(Long idSolicitudAlquiler) {
        this.idSolicitudAlquiler = idSolicitudAlquiler;
    }

    public PerfilEstudianteEntity getPerfilEstudiante() {
        return perfilEstudiante;
    }

    public void setPerfilEstudiante(PerfilEstudianteEntity perfilEstudiante) {
        this.perfilEstudiante = perfilEstudiante;
    }

    public PropiedadEntity getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(PropiedadEntity propiedad) {
        this.propiedad = propiedad;
    }

    public Situacion getSituacion() {
        return switch (estado) {
            case "ACEPTADA" -> Situacion.ACEPTADA;
            case "RECHAZADA", "CANCELADA", "false" -> Situacion.CANCELADA;
            default -> Situacion.PENDIENTE;
        };
    }

    public void resolver(Situacion decision) {
        estado = decision.name();
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}