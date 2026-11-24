package com.findimo.api.Service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.findimo.api.Dto.PropiedadRequestDto;
import com.findimo.api.Dto.PropiedadResponseDto;
import com.findimo.api.Entity.PerfilArrendadorEntity;
import com.findimo.api.Entity.PropiedadEntity;
import com.findimo.api.Repository.PerfilArrendadorRepository;
import com.findimo.api.Repository.PropiedadRepository;
import com.findimo.api.Service.PropiedadService;

@Service
public class PropiedadServiceImpl implements PropiedadService {

	@Autowired
	private PropiedadRepository propiedadRepository;

	@Autowired
	private PerfilArrendadorRepository perfilArrendadorRepository;

	@Override
	public PropiedadResponseDto crearPropiedad(PropiedadRequestDto request) {

		PerfilArrendadorEntity perfilArrendador =
				perfilArrendadorRepository.getReferenceById(request.getIdPerfilArrendador());

		PropiedadEntity propiedad = new PropiedadEntity();

		propiedad.setTitulo(request.getTitulo());
		propiedad.setDescripcion(request.getDescripcion());
		propiedad.setPrecio(request.getPrecio());
		propiedad.setHabitaciones(request.getHabitaciones());
		propiedad.setDireccion(request.getDireccion());
		propiedad.setDistrito(request.getDistrito());
		propiedad.setEstado(request.getEstado());
		propiedad.setPerfilArrendador(perfilArrendador);

		PropiedadEntity propiedadGuardada = propiedadRepository.save(propiedad);

		PropiedadResponseDto response = new PropiedadResponseDto();

		response.setIdPropiedad(propiedadGuardada.getIdPropiedad());
		response.setTitulo(propiedadGuardada.getTitulo());
		response.setDescripcion(propiedadGuardada.getDescripcion());
		response.setPrecio(propiedadGuardada.getPrecio());
		response.setHabitaciones(propiedadGuardada.getHabitaciones());
		response.setDireccion(propiedadGuardada.getDireccion());
		response.setDistrito(propiedadGuardada.getDistrito());
		response.setEstado(propiedadGuardada.getEstado());

		return response;
	}

	@Override
	public List<PropiedadResponseDto> obtenerTodas() {

		List<PropiedadEntity> propiedades = propiedadRepository.findAll();

		List<PropiedadResponseDto> response = new ArrayList<>();

		for (PropiedadEntity propiedad : propiedades) {

			PropiedadResponseDto dto = new PropiedadResponseDto();

			dto.setIdPropiedad(propiedad.getIdPropiedad());
			dto.setTitulo(propiedad.getTitulo());
			dto.setDescripcion(propiedad.getDescripcion());
			dto.setPrecio(propiedad.getPrecio());
			dto.setHabitaciones(propiedad.getHabitaciones());
			dto.setDireccion(propiedad.getDireccion());
			dto.setDistrito(propiedad.getDistrito());
			dto.setEstado(propiedad.getEstado());

			response.add(dto);
		}

		return response;
	}
}