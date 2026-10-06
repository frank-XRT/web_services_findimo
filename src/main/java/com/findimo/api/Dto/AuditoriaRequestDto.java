package com.findimo.api.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaRequestDto {

    private Long idUsuarioEditar;

    private Long idUsuarioEliminar;

    private String datosAnteriores;

    private String datosNuevos;

    private Boolean estado;
}