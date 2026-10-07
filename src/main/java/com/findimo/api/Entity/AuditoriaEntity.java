package com.findimo.api.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "tt_auditoria", schema = "findimo")
public class AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDAUDITORIA")
    private Long idAuditoria;

    @Column(name = "IDUSUARIOEDITAR")
    private Long idUsuarioEditar;

    @Column(name = "IDUSUARIOELIMINAR")
    private Long idUsuarioEliminar;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "DATOSANTERIORES", columnDefinition = "jsonb")
    private Map<String, Object> datosAnteriores;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "DATOSNUEVOS", columnDefinition = "jsonb")
    private Map<String, Object> datosNuevos;

    @Column(
            name = "FECHAREGISTRO",
            insertable = false,
            updatable = false
    )
    private LocalDateTime fechaRegistro;

    @Column(name = "ESTADO", nullable = false)
    private Boolean estado;

    public AuditoriaEntity() {
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