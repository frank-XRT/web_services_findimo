package com.findimo.api.Dto;

import lombok.Data;

public class FavoritoRequestDto {

    private Long idPerfilEstudiante;
    private Long idPropiedad;

    public FavoritoRequestDto() {
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