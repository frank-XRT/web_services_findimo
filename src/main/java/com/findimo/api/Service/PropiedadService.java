package com.findimo.api.Service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.findimo.api.Dto.PropiedadRequestDto;
import com.findimo.api.Dto.PropiedadResponseDto;

public interface PropiedadService {

	PropiedadResponseDto crearPropiedad(PropiedadRequestDto request, List<MultipartFile> fotos);

	PropiedadResponseDto actualizarPropiedad(Long idPropiedad, Long idArrendador, PropiedadRequestDto request, List<MultipartFile> fotos);

	List<PropiedadResponseDto> obtenerTodas();
}
