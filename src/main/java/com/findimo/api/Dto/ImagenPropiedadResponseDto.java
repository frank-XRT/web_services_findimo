package com.findimo.api.Dto;

import java.time.LocalDateTime;

public class ImagenPropiedadResponseDto {

	private Long idImagenPropiedad;
	private Long idPropiedad;
	private String rutaImagen;
	private Boolean principal;
	private LocalDateTime fechaCreacion;
	private Boolean estado;

	public ImagenPropiedadResponseDto() {
	}

	public Long getIdImagenPropiedad() {
		return idImagenPropiedad;
	}

	public void setIdImagenPropiedad(Long idImagenPropiedad) {
		this.idImagenPropiedad = idImagenPropiedad;
	}

	public Long getIdPropiedad() {
		return idPropiedad;
	}

	public void setIdPropiedad(Long idPropiedad) {
		this.idPropiedad = idPropiedad;
	}

	public String getRutaImagen() {
		return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
}
