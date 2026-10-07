package com.findimo.api.Entity;

import java.math.BigDecimal;

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
@Table(name = "tm_propiedad", schema = "findimo")
public class PropiedadEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDPROPIEDAD")
	private Long idPropiedad;

	@Column(name = "TITULO")
	private String titulo;

	@Column(name = "DESCRIPCION", columnDefinition = "TEXT")
	private String descripcion;

	@Column(name = "PRECIO")
	private BigDecimal precio;

	@Column(name = "HABITACIONES")
	private Integer habitaciones;

	@Column(name = "DIRECCION")
	private String direccion;

	@Column(name = "DISTRITO")
	private String distrito;

	@Column(name = "ESTADO")
	private Boolean estado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPerfilArrendador", referencedColumnName = "idPerfilArrendador")
	private PerfilArrendadorEntity perfilArrendador;

	public PropiedadEntity() {
	}

	public Long getIdPropiedad() {
		return idPropiedad;
	}

	public void setIdPropiedad(Long idPropiedad) {
		this.idPropiedad = idPropiedad;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public Integer getHabitaciones() {
		return habitaciones;
	}

	public void setHabitaciones(Integer habitaciones) {
		this.habitaciones = habitaciones;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public PerfilArrendadorEntity getPerfilArrendador() {
		return perfilArrendador;
	}

	public void setPerfilArrendador(PerfilArrendadorEntity perfilArrendador) {
		this.perfilArrendador = perfilArrendador;
	}
}