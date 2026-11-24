package com.findimo.api.Dto;

import java.util.Map;

public class AuditoriaRequestDto {

    private Long idUsuarioEditar;

    private Long idUsuarioEliminar;

    private Map<String, Object> datosAnteriores;

    private Map<String, Object> datosNuevos;

    private Boolean estado;

    public AuditoriaRequestDto() {
    }

    public Long getIdUsuarioEditar() {
        return idUsuarioEditar;
    }

    public void setIdUsuarioEditar(Long idUsuarioEditar) {
        this.idUsuarioEditar = idUsuarioEditar;
    }

    public Long getIdUsuarioEliminar() {
        return idUsuarioEliminar;
    }

    public void setIdUsuarioEliminar(Long idUsuarioEliminar) {
        this.idUsuarioEliminar = idUsuarioEliminar;
    }

    public Map<String, Object> getDatosAnteriores() {
        return datosAnteriores;
    }

    public void setDatosAnteriores(
            Map<String, Object> datosAnteriores) {
        this.datosAnteriores = datosAnteriores;
    }

    public Map<String, Object> getDatosNuevos() {
        return datosNuevos;
    }

    public void setDatosNuevos(
            Map<String, Object> datosNuevos) {
        this.datosNuevos = datosNuevos;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}