package com.findimo.api.Service;

import java.util.List;

import com.findimo.api.Dto.FavoritoRequestDto;
import com.findimo.api.Dto.FavoritoResponseDto;

public interface FavoritoService {

    FavoritoResponseDto registrarFavorito(FavoritoRequestDto dto);

    FavoritoResponseDto cambiarEstadoFavorito(Long idFavorito, Boolean estado);

    List<FavoritoResponseDto> listarFavoritosPorPerfilEstudiante(
            Long idPerfilEstudiante
    );
}