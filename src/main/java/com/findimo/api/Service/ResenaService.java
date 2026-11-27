package com.findimo.api.Service;

import java.util.List;

import com.findimo.api.Dto.ResenaRequestDto;
import com.findimo.api.Dto.ResenaResponseDto;

import com.findimo.api.Dto.ResenasPerfilDto;

public interface ResenaService {

    ResenasPerfilDto listarResenasDePerfil(Long idUsuario);

    ResenaResponseDto registrarResena(ResenaRequestDto dto);

    List<ResenaResponseDto> listarResenasPorPropiedad(Long idPropiedad);

    List<ResenaResponseDto> listarResenasDelArrendador(Long idPropiedad);
}