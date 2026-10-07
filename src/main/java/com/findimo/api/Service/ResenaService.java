package com.findimo.api.Service;

import java.util.List;

import com.findimo.api.Dto.ResenaRequestDto;
import com.findimo.api.Dto.ResenaResponseDto;

public interface ResenaService {

    ResenaResponseDto registrarResena(ResenaRequestDto dto);

    List<ResenaResponseDto> listarResenasPorPropiedad(Long idPropiedad);
}