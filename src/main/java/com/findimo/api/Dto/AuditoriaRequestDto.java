package com.findimo.api.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaRequestDto {

    private Long idUsuarioEditar;

    private Long idUsuarioEliminar;

    private Map<String, Object> datosAnteriores;

    private Map<String, Object> datosNuevos;

    private Boolean estado;
}