package com.findimo.api.Dto;

import java.time.LocalDateTime;

public class SolicitudAlquilerResponseDto {

    private Long idSolicitudAlquiler;
    private Long idPerfilEstudiante;
    private Long idPropiedad;
    private Boolean estado;
    private LocalDateTime fechaCreacion;

    public SolicitudAlquilerResponseDto() {
    }

    public Long getIdSolicitudAlquiler() {
        return idSolicitudAlquiler;
    }

    public void setIdSolicitudAlquiler(Long idSolicitudAlquiler) {
        this.idSolicitudAlquiler = idSolicitudAlquiler;
    }

    public Long getIdPerfilEstudiante() {
        return idPerfilEstudiante;
    }

    public void setIdPerfilEstudiante(Long idPerfilEstudiante) {
        this.idPerfilEstudiante = idPerfilEstudiante;
    }

    public Long getIdPropiedad() {
        return idPropiedad;
    }

    public void setIdPropiedad(Long idPropiedad) {
        this.idPropiedad = idPropiedad;
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