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

import java.time.LocalDateTime;

@Entity
@Table(name = "tt_auditoria", schema = "findimo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idauditoria")
    private Long idAuditoria;

    @Column(name = "idusuarioeditar")
    private Long idUsuarioEditar;

    @Column(name = "idusuarioeliminar")
    private Long idUsuarioEliminar;

    @Column(name = "datosanteriores", columnDefinition = "jsonb")
    private String datosAnteriores;

    @Column(name = "datosnuevos", columnDefinition = "jsonb")
    private String datosNuevos;

    @Column(name = "fecharegistro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "estado", nullable = false)
    private Boolean estado;
}