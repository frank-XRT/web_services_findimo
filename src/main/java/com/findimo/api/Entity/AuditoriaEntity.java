package com.findimo.api.Entity;

import java.time.LocalDateTime;

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

    @Column(name = "DATOSANTERIORES", columnDefinition = "jsonb")
    private String datosAnteriores;

    @Column(name = "DATOSNUEVOS", columnDefinition = "jsonb")
    private String datosNuevos;

    @Column(
            name = "FECHAREGISTRO",
            insertable = false,
            updatable = false
    )
    private LocalDateTime fechaRegistro;

    @Column(name = "ESTADO", nullable = false)
    private Boolean estado;
}