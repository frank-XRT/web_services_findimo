package com.findimo.api.Service.Impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import com.findimo.api.Service.AlmacenFotos;

import com.findimo.api.Dto.PropiedadRequestDto;
import com.findimo.api.Dto.PropiedadResponseDto;
import com.findimo.api.Entity.LugarCercano;
import com.findimo.api.Entity.ImagenPropiedadEntity;
import com.findimo.api.Entity.PerfilArrendadorEntity;
import com.findimo.api.Entity.PropiedadEntity;
import com.findimo.api.Repository.PerfilArrendadorRepository;
import com.findimo.api.Repository.PropiedadRepository;
import com.findimo.api.Repository.CatalogoPropiedadRepository;
import com.findimo.api.Repository.ImagenPropiedadRepository;
import com.findimo.api.Repository.ResenaRepository;
import com.findimo.api.Repository.ResenaRepository.ValoracionPropiedad;
import com.findimo.api.Service.PropiedadService;
import com.findimo.api.Repository.SolicitudAlquilerRepository;
import com.findimo.api.Entity.SolicitudAlquilerEntity.Reserva;

@Service
public class PropiedadServiceImpl implements PropiedadService {

	@Autowired
	private PropiedadRepository propiedadRepository;

	@Autowired
	private PerfilArrendadorRepository perfilArrendadorRepository;

	@Autowired
	private ResenaRepository resenaRepository;

	@Autowired
	private ImagenPropiedadRepository imagenRepository;

	@Autowired
	private AlmacenFotos almacenFotos;

	@Autowired
	private CatalogoPropiedadRepository catalogo;

	@Autowired
	private SolicitudAlquilerRepository solicitudes;

	@Override
	@Transactional
	public PropiedadResponseDto crearPropiedad(PropiedadRequestDto request, List<MultipartFile> fotos) {

		PerfilArrendadorEntity perfilArrendador =
				perfilArrendadorRepository.getReferenceById(request.getIdPerfilArrendador());

		PropiedadEntity propiedad = new PropiedadEntity();

		propiedad.setEstado(request.getEstado());
		propiedad.setPerfilArrendador(perfilArrendador);
		aplicarDatos(propiedad, request);
		var guardada = propiedadRepository.save(propiedad);
		return guardarFotos(guardada, fotos, List.of());
	}

	@Override
	@Transactional
	public PropiedadResponseDto actualizarPropiedad(Long idPropiedad, Long idArrendador,
			PropiedadRequestDto request, List<MultipartFile> fotos) {
		var propiedad = propiedadRepository.findByIdPropiedadAndPerfilArrendador_IdPerfilArrendador(idPropiedad, idArrendador)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (!Boolean.TRUE.equals(propiedad.getEstado())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "La vivienda está retirada.");
		}
		aplicarDatos(propiedad, request);
		var imagenes = imagenRepository.findByPropiedad_IdPropiedadAndEstadoTrueOrderByPrincipalDescIdImagenPropiedadAsc(idPropiedad);
		var existentes = retirarFotos(imagenes, request.getFotosRetiradas());
		var respuesta = guardarFotos(propiedad, fotos, existentes);
		respuesta.setReservada(solicitudes.existsByPropiedad_IdPropiedadAndReserva(idPropiedad, Reserva.CONFIRMADA));
		return respuesta;
	}

	private List<String> retirarFotos(List<ImagenPropiedadEntity> imagenes, List<String> rutasRetiradas) {
		var retirar = rutasRetiradas == null ? new HashSet<String>() : new HashSet<>(rutasRetiradas);
		var restantes = new ArrayList<ImagenPropiedadEntity>();
		for (var imagen : imagenes) {
			if (retirar.contains(imagen.getRutaImagen())) {
				imagen.setEstado(false);
				imagen.setPrincipal(false);
			} else {
				restantes.add(imagen);
			}
		}

		if (!retirar.isEmpty() && !restantes.isEmpty()) {
			restantes.getFirst().setPrincipal(true);
		}
		return restantes.stream().map(ImagenPropiedadEntity::getRutaImagen).toList();
	}

	private void aplicarDatos(PropiedadEntity propiedad, PropiedadRequestDto request) {
		if (request.getTitulo() == null || request.getTitulo().isBlank()
				|| request.getDescripcion() == null || request.getDescripcion().isBlank()
				|| request.getPrecio() == null || request.getPrecio().signum() <= 0
				|| request.getHabitaciones() == null || request.getHabitaciones() < 1
				|| request.getDireccion() == null || request.getDireccion().isBlank()
				|| request.getDistrito() == null || request.getDistrito().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Completa los datos obligatorios de la vivienda.");
		}
		propiedad.setTitulo(request.getTitulo().strip());
		propiedad.setDescripcion(request.getDescripcion().strip());
		propiedad.setPrecio(request.getPrecio());
		propiedad.setHabitaciones(request.getHabitaciones());
		BigDecimal area = request.getArea();
		if (area == null || area.signum() <= 0 || area.stripTrailingZeros().scale() > 2
				|| area.compareTo(new BigDecimal("999999.99")) > 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"El área es obligatoria y debe ser positiva, con hasta dos decimales.");
		}
		propiedad.setArea(area);
		propiedad.setDireccion(request.getDireccion().strip());
		propiedad.setDistrito(request.getDistrito().strip());
		if (request.getEntorno() != null) propiedad.setEntorno(validarEntorno(request.getEntorno()));
		if (request.getServiciosPersonalizados() != null) {
			propiedad.setServiciosPersonalizados(validarServiciosPersonalizados(request.getServiciosPersonalizados()));
		}
		aplicarCatalogos(propiedad, request);
	}

	private void aplicarCatalogos(PropiedadEntity propiedad, PropiedadRequestDto request) {
		if (!request.tieneTipo() && request.getIdsServicios() == null) return;

		var opciones = catalogo.obtener();
		if (request.tieneTipo()) {
			var tipo = request.getIdTipoPropiedad();
			if (tipo != null && !tipo.equals(propiedad.getIdTipoPropiedad())
					&& opciones.tipos().stream().noneMatch(opcion -> opcion.id().equals(tipo))) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo de propiedad no está disponible.");
			}
			propiedad.setIdTipoPropiedad(tipo);
		}
		if (request.getIdsServicios() != null) {
			var serviciosActivos = opciones.servicios().stream().map(CatalogoPropiedadRepository.Opcion::id)
					.collect(Collectors.toSet());
			serviciosActivos.addAll(propiedad.getIdsServicios());
			if (!serviciosActivos.containsAll(request.getIdsServicios())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Algún servicio no está disponible.");
			}
			propiedad.getIdsServicios().retainAll(request.getIdsServicios());
			propiedad.getIdsServicios().addAll(request.getIdsServicios());
		}
	}

	private PropiedadResponseDto guardarFotos(PropiedadEntity propiedad, List<MultipartFile> fotos, List<String> existentes) {
		if (fotos.isEmpty() && existentes.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La vivienda requiere al menos una foto.");
		}
		var nuevas = almacenFotos.guardar(fotos);
		for (int i = 0; i < nuevas.size(); i++) {
			var imagen = new ImagenPropiedadEntity();
			imagen.setPropiedad(propiedad);
			imagen.setRutaImagen(nuevas.get(i));
			imagen.setPrincipal(existentes.isEmpty() && i == 0);
			imagenRepository.save(imagen);
		}
		var rutas = new ArrayList<>(existentes);
		rutas.addAll(nuevas);
		var respuesta = convertir(propiedad);
		respuesta.setImagenes(rutas);
		return respuesta;
	}

	private List<LugarCercano> validarEntorno(List<LugarCercano> lugares) {
		if (lugares == null || lugares.isEmpty()) {
			return List.of();
		}

		if (lugares.size() > 30) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Puedes agregar hasta 30 lugares cercanos.");
		var entorno = new ArrayList<LugarCercano>();
		for (LugarCercano lugar : lugares) {
			if (lugar == null || lugar.tipo() == null
					|| lugar.minutos() == null || lugar.minutos() < 1 || lugar.minutos() > 1440
					|| (lugar.nombre() != null && lugar.nombre().length() > 120)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Cada lugar debe tener una categoría y entre 1 y 1440 minutos.");
			}
			String nombre = lugar.nombre() == null ? "" : lugar.nombre().strip();
			String categoria = null;
			if (lugar.tipo() == LugarCercano.Tipo.PERSONALIZADO) {
				categoria = lugar.categoria() == null ? "" : lugar.categoria().strip();
				if (categoria.isEmpty() || categoria.length() > 60) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indica una categoría de hasta 60 caracteres.");
				}
			}
			entorno.add(new LugarCercano(lugar.tipo(), nombre.isEmpty() ? null : nombre, lugar.minutos(), categoria));
		}
		return entorno;
	}

	private List<String> validarServiciosPersonalizados(List<String> servicios) {
		if (servicios.size() > 30) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Puedes agregar hasta 30 servicios personalizados.");
		return servicios.stream().map(servicio -> {
			if (servicio == null || servicio.isBlank() || servicio.strip().length() > 80) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cada servicio debe tener un nombre de hasta 80 caracteres.");
			}
			return servicio.strip();
		}).distinct().toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PropiedadResponseDto> obtenerTodas() {

		List<PropiedadEntity> propiedades = propiedadRepository.findByEstadoTrue();

		var valoraciones = resenaRepository.resumirResenasPorPropiedad().stream()
				.collect(Collectors.toMap(ValoracionPropiedad::getIdPropiedad, Function.identity()));
		var imagenes = imagenRepository.findByEstadoTrueOrderByPrincipalDescIdImagenPropiedadAsc().stream()
				.collect(Collectors.groupingBy(imagen -> imagen.getPropiedad().getIdPropiedad(),
						Collectors.mapping(ImagenPropiedadEntity::getRutaImagen, Collectors.toList())));
		var reservadas = new HashSet<>(solicitudes.idsPropiedadesPorReserva(Reserva.CONFIRMADA));
		List<PropiedadResponseDto> response = new ArrayList<>();

		for (PropiedadEntity propiedad : propiedades) {

			PropiedadResponseDto dto = convertir(propiedad);
			dto.setReservada(reservadas.contains(propiedad.getIdPropiedad()));
			dto.setImagenes(imagenes.getOrDefault(propiedad.getIdPropiedad(), List.of()));

			ValoracionPropiedad valoracion = valoraciones.get(propiedad.getIdPropiedad());
			if (valoracion != null) {
				dto.setCalificacion(valoracion.getCalificacion());
				dto.setTotalResenas(valoracion.getTotalResenas());
			}

			response.add(dto);
		}

		return response;
	}

	private PropiedadResponseDto convertir(PropiedadEntity propiedad) {
		var dto = new PropiedadResponseDto();
		dto.setIdPropiedad(propiedad.getIdPropiedad());
		dto.setTitulo(propiedad.getTitulo());
		dto.setDescripcion(propiedad.getDescripcion());
		dto.setPrecio(propiedad.getPrecio());
		dto.setHabitaciones(propiedad.getHabitaciones());
		dto.setArea(propiedad.getArea());
		dto.setIdTipoPropiedad(propiedad.getIdTipoPropiedad());
		dto.setIdsServicios(Set.copyOf(propiedad.getIdsServicios()));
		dto.setDireccion(propiedad.getDireccion());
		dto.setDistrito(propiedad.getDistrito());
		dto.setEstado(propiedad.getEstado());
		dto.setEntorno(propiedad.getEntorno() == null ? List.of() : propiedad.getEntorno());
		dto.setServiciosPersonalizados(propiedad.getServiciosPersonalizados() == null ? List.of() : propiedad.getServiciosPersonalizados());
		var arrendador = propiedad.getPerfilArrendador();
		if (arrendador != null) {
			dto.setIdPerfilArrendador(arrendador.getIdPerfilArrendador());
		}
		if (arrendador != null && arrendador.getUsuario() != null) {
			var usuario = arrendador.getUsuario();
			dto.setArrendadorNombre(String.join(" ", usuario.getNombre() == null ? "" : usuario.getNombre(),
					usuario.getApellido() == null ? "" : usuario.getApellido()).strip());
		}
		return dto;
	}
}
