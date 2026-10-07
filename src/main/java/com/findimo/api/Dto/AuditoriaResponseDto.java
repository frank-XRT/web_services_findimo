package com.findimo.api.Dto;

import java.time.LocalDateTime;
import java.util.Map;

public class AuditoriaResponseDto {

    private Long idAuditoria;

    private Long idUsuarioEditar;

    private Long idUsuarioEliminar;

    private Map<String, Object> datosAnteriores;

    private Map<String, Object> datosNuevos;

    private LocalDateTime fechaRegistro;

    private Boolean estado;

    public AuditoriaResponseDto() {
    }

    public Long getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(Long idAuditoria) {
        this.idAuditoria = idAuditoria;
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

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(
            LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}