package com.findimo.api.Dto;

public class SolicitudAlquilerRequestDto {

    private Long idPerfilEstudiante;
    private Long idPropiedad;

    public SolicitudAlquilerRequestDto() {
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
}