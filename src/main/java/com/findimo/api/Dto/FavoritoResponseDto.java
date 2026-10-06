package com.findimo.api.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritoResponseDto {

    private Long idFavorito;

    private Long idPerfilEstudiante;

    private Long idPropiedad;

    private Boolean estado;
}