package com.findimo.api.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tt_imagen_propiedad", schema = "findimo")
public class ImagenPropiedadEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDIMAGENPROPIEDAD")
	private Long idImagenPropiedad;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPropiedad", referencedColumnName = "IDPROPIEDAD", nullable = false)
	private PropiedadEntity propiedad;

	@Column(name = "RUTAIMAGEN", length = 500, nullable = false)
	private String rutaImagen;

	@Column(name = "PRINCIPAL", nullable = false)
	private Boolean principal = false;

	@Column(name = "FECHACREACION", insertable = false, updatable = false)
	private LocalDateTime fechaCreacion;

	@Column(name = "ESTADO", nullable = false)
	private Boolean estado = true;

	public ImagenPropiedadEntity() {
	}

	public Long getIdImagenPropiedad() {
		return idImagenPropiedad;
	}

	public void setIdImagenPropiedad(Long idImagenPropiedad) {
		this.idImagenPropiedad = idImagenPropiedad;
	}

	public PropiedadEntity getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(PropiedadEntity propiedad) {
		this.propiedad = propiedad;
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
