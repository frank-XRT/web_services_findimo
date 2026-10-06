package com.findimo.api.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaResponseDto {

    private Long idAuditoria;

    private Long idUsuarioEditar;

    private Long idUsuarioEliminar;

    private String datosAnteriores;

    private String datosNuevos;

    private LocalDateTime fechaRegistro;

    private Boolean estado;
}