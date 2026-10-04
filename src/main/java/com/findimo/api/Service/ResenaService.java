package com.findimo.api.Service;

import java.util.List;

import com.findimo.api.Dto.ResenaResponseDto;

public interface ResenaService {

    List<ResenaResponseDto> listarResenasPorPropiedad(Long idPropiedad);
}