package com.findimo.api.Service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.findimo.api.Dto.PropiedadRequestDto;
import com.findimo.api.Entity.PerfilArrendadorEntity;
import com.findimo.api.Entity.PropiedadEntity;
import com.findimo.api.Repository.PerfilArrendadorRepository;
import com.findimo.api.Repository.PropiedadRepository;
import com.findimo.api.Dto.PropiedadResponseDto;
import com.findimo.api.Service.AuditoriaService;
import com.findimo.api.Service.PropiedadService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PropiedadServiceImpl implements PropiedadService {

	private final PropiedadRepository propiedadRepository;

	private final PerfilArrendadorRepository perfilArrendadorRepository;

	private final AuditoriaService auditoriaService;


	@Override
	public PropiedadResponseDto crearPropiedad(
			PropiedadRequestDto request) {

		// Validar usuario
		if (request.getIdUsuario() == null) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"El id del usuario es obligatorio"
			);
		}

		// Validar perfil de arrendador
		if (request.getIdPerfilArrendador() == null) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"El id del perfil de arrendador es obligatorio"
			);
		}


		// Buscar perfil de arrendador
		PerfilArrendadorEntity perfilArrendador =
				perfilArrendadorRepository
						.findById(request.getIdPerfilArrendador())
						.orElseThrow(() -> new ResponseStatusException(
								HttpStatus.NOT_FOUND,
								"No existe el perfil de arrendador indicado"
						));


		// Crear propiedad
		PropiedadEntity propiedad =
				PropiedadEntity.builder()
						.titulo(request.getTitulo())
						.descripcion(request.getDescripcion())
						.precio(request.getPrecio())
						.habitaciones(request.getHabitaciones())
						.direccion(request.getDireccion())
						.distrito(request.getDistrito())
						.estado(request.getEstado())
						.perfilArrendador(perfilArrendador)
						.build();


		// Guardar propiedad
		PropiedadEntity propiedadGuardada =
				propiedadRepository.save(propiedad);


		// =========================
		// AUDITORÍA
		// =========================

		Map<String, Object> datosNuevos =
				new HashMap<>();

		datosNuevos.put(
				"tabla",
				"tm_propiedad"
		);

		datosNuevos.put(
				"accion",
				"CREAR"
		);

		datosNuevos.put(
				"idPropiedad",
				propiedadGuardada.getIdPropiedad()
		);

		datosNuevos.put(
				"idPerfilArrendador",
				request.getIdPerfilArrendador()
		);

		datosNuevos.put(
				"titulo",
				propiedadGuardada.getTitulo()
		);

		datosNuevos.put(
				"descripcion",
				propiedadGuardada.getDescripcion()
		);

		datosNuevos.put(
				"precio",
				propiedadGuardada.getPrecio()
		);

		datosNuevos.put(
				"habitaciones",
				propiedadGuardada.getHabitaciones()
		);

		datosNuevos.put(
				"direccion",
				propiedadGuardada.getDireccion()
		);

		datosNuevos.put(
				"distrito",
				propiedadGuardada.getDistrito()
		);

		datosNuevos.put(
				"estado",
				propiedadGuardada.getEstado()
		);


		// Registrar auditoría
		auditoriaService.registrarAuditoria(
				request.getIdUsuario(),
				null,
				null,
				datosNuevos
		);


		// Respuesta
		return toResponseDto(propiedadGuardada);
	}


	@Override
	public List<PropiedadResponseDto> obtenerTodas() {

		return propiedadRepository.findAll()
				.stream()
				.map(this::toResponseDto)
				.toList();
	}


	private PropiedadResponseDto toResponseDto(
			PropiedadEntity propiedad) {

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