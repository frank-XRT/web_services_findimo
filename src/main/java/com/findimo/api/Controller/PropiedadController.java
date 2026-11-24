package com.findimo.api.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.findimo.api.Dto.PropiedadRequestDto;
import com.findimo.api.Dto.PropiedadResponseDto;
import com.findimo.api.Service.PropiedadService;

@RestController
@RequestMapping("/api/propiedades")
public class PropiedadController {

	@Autowired
	private PropiedadService propiedadService;

	@PostMapping
	public PropiedadResponseDto crearPropiedad(@RequestBody PropiedadRequestDto request) {

		PropiedadResponseDto propiedad = propiedadService.crearPropiedad(request);

		return propiedad;
	}

	@GetMapping
	public List<PropiedadResponseDto> obtenerTodas() {

		List<PropiedadResponseDto> propiedades = propiedadService.obtenerTodas();

		return propiedades;
	}
}