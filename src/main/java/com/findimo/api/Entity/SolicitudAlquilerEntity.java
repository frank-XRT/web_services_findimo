package com.findimo.api.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private Boolean estado;

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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}