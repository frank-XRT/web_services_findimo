package com.findimo.api.Service;

import java.util.List;

import com.findimo.api.Dto.PropiedadRequestDto;
import com.findimo.api.Dto.PropiedadResponseDto;

public interface PropiedadService {

	PropiedadResponseDto crearPropiedad(PropiedadRequestDto request);

	List<PropiedadResponseDto> obtenerTodas();
}
