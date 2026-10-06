package com.findimo.api.Dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudAlquilerResponseDto {

    private Long idSolicitudAlquiler;

    private Long idPerfilEstudiante;

    private Long idPropiedad;

    private Boolean estado;

    private LocalDateTime fechaCreacion;
}