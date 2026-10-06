package com.findimo.api.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "tt_auditoria", schema = "findimo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}