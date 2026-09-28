package com.findimo.api.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.findimo.api.Dto.PropiedadRequestDto;
import com.findimo.api.Dto.PropiedadResponseDto;
import com.findimo.api.Service.PropiedadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/propiedades")
@RequiredArgsConstructor
public class PropiedadController {

	private final PropiedadService propiedadService;

	@PostMapping
	public ResponseEntity<PropiedadResponseDto> crearPropiedad(
			@RequestBody PropiedadRequestDto request) {
		PropiedadResponseDto propiedad = propiedadService.crearPropiedad(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(propiedad);
	}

	@GetMapping
	public List<PropiedadResponseDto> obtenerTodas() {
		return propiedadService.obtenerTodas();
	}
}
