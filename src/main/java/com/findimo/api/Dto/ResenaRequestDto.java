package com.findimo.api.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResenaRequestDto {

    private Long idUsuarioAutor;

    private Long idPropiedad;

    private Long idUsuarioObjetivo;

    private Integer calificacion;

    private String comentario;
}