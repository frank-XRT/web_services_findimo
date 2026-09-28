package com.findimo.api.Service.Impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.findimo.api.Entity.PerfilArrendadorEntity;
import com.findimo.api.Entity.PropiedadEntity;
import com.findimo.api.Repository.PerfilArrendadorRepository;
import com.findimo.api.Repository.PropiedadRepository;
import com.findimo.api.Dto.PropiedadRequestDto;
import com.findimo.api.Dto.PropiedadResponseDto;
import com.findimo.api.Service.PropiedadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropiedadServiceImpl implements PropiedadService {

	private final PropiedadRepository propiedadRepository;
	private final PerfilArrendadorRepository perfilArrendadorRepository;

	@Override
	public PropiedadResponseDto crearPropiedad(PropiedadRequestDto request) {
		if (request.getIdPerfilArrendador() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id del perfil de arrendador es obligatorio");
		}

		PerfilArrendadorEntity perfilArrendador = perfilArrendadorRepository
				.findById(request.getIdPerfilArrendador())
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, "No existe el perfil de arrendador indicado"));

		PropiedadEntity propiedad = PropiedadEntity.builder()
				.titulo(request.getTitulo())
				.descripcion(request.getDescripcion())
				.precio(request.getPrecio())
				.habitaciones(request.getHabitaciones())
				.direccion(request.getDireccion())
				.distrito(request.getDistrito())
				.estado(request.getEstado())
				.perfilArrendador(perfilArrendador)
				.build();

		return toResponseDto(propiedadRepository.save(propiedad));
	}

	@Override
	public List<PropiedadResponseDto> obtenerTodas() {
		return propiedadRepository.findAll().stream()
				.map(this::toResponseDto)
				.toList();
	}

	private PropiedadResponseDto toResponseDto(PropiedadEntity propiedad) {
		return PropiedadResponseDto.builder()
				.idPropiedad(propiedad.getIdPropiedad())
				.titulo(propiedad.getTitulo())
				.descripcion(propiedad.getDescripcion())
				.precio(propiedad.getPrecio())
				.habitaciones(propiedad.getHabitaciones())
				.direccion(propiedad.getDireccion())
				.distrito(propiedad.getDistrito())
				.estado(propiedad.getEstado())
				.build();
	}
}
