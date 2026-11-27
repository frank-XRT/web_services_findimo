package com.findimo.api.Controller;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.findimo.api.Repository.PerfilArrendadorRepository;
import com.findimo.api.Repository.CatalogoPropiedadRepository;

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

	@Autowired
	private PerfilArrendadorRepository arrendadores;

	@Autowired
	private CatalogoPropiedadRepository catalogo;

	@GetMapping("/catalogos")
	public CatalogoPropiedadRepository.Catalogos obtenerCatalogos() {
		return catalogo.obtener();
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public PropiedadResponseDto crearPropiedad(@RequestBody PropiedadRequestDto dto, HttpServletRequest request) {
		return guardar(dto, List.of(), request);
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public PropiedadResponseDto publicarConFotos(@RequestPart("propiedad") PropiedadRequestDto dto,
			@RequestPart(value = "fotos", required = false) List<MultipartFile> fotos,
			HttpServletRequest request) {
		return guardar(dto, fotos == null ? List.of() : fotos, request);
	}

	private PropiedadResponseDto guardar(PropiedadRequestDto dto, List<MultipartFile> fotos,
			HttpServletRequest request) {
		dto.setIdPerfilArrendador(arrendadorAutenticado(request));
		return propiedadService.crearPropiedad(dto, fotos);
	}

	@PutMapping(path = "/{idPropiedad}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public PropiedadResponseDto actualizar(@PathVariable("idPropiedad") Long idPropiedad,
			@RequestPart("propiedad") PropiedadRequestDto dto,
			@RequestPart(value = "fotos", required = false) List<MultipartFile> fotos,
			HttpServletRequest request) {
		Long idArrendador = arrendadorAutenticado(request);
		return propiedadService.actualizarPropiedad(idPropiedad, idArrendador, dto, fotos == null ? List.of() : fotos);
	}

	private Long arrendadorAutenticado(HttpServletRequest request) {
		var sesion = request.getSession(false);
		if (sesion == null || !(sesion.getAttribute(UsuarioController.USUARIO_EN_SESION) instanceof Long cuenta)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		var arrendador = arrendadores.findByUsuario_IdUsuario(cuenta)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
		return arrendador.getIdPerfilArrendador();
	}

	@GetMapping
	public List<PropiedadResponseDto> obtenerTodas() {

		List<PropiedadResponseDto> propiedades = propiedadService.obtenerTodas();

		return propiedades;
	}
}