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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tt_solicitud_alquiler", schema = "findimo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudAlquilerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDSOLICITUDALQUILER")
    private Long idSolicitudAlquiler;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "IDPERFILESTUDIANTE",
            nullable = false
    )
    private PerfilEstudianteEntity perfilEstudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "IDPROPIEDAD",
            nullable = false
    )
    private PropiedadEntity propiedad;

    @Column(name = "ESTADO", nullable = false)
    private Boolean estado;

    @Column(
            name = "FECHACREACION",
            insertable = false,
            updatable = false
    )
    private LocalDateTime fechaCreacion;
}