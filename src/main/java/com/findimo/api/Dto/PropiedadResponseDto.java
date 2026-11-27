package com.findimo.api.Dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.findimo.api.Entity.LugarCercano;

public class PropiedadResponseDto {

	private Integer idTipoPropiedad;
	private Set<Integer> idsServicios;

	public Integer getIdTipoPropiedad() {
		return idTipoPropiedad;
	}

	public void setIdTipoPropiedad(Integer idTipoPropiedad) {
		this.idTipoPropiedad = idTipoPropiedad;
	}

	public Set<Integer> getIdsServicios() {
		return idsServicios;
	}

	public void setIdsServicios(Set<Integer> idsServicios) {
		this.idsServicios = idsServicios;
	}

	private Long idPropiedad;
	private String titulo;
	private String descripcion;
	private BigDecimal precio;
	private Integer habitaciones;
	private BigDecimal area;
	private String direccion;
	private String distrito;
	private Boolean estado;
	private boolean reservada;

	public boolean isReservada() {
		return reservada;
	}

	public void setReservada(boolean reservada) {
		this.reservada = reservada;
	}
	private List<LugarCercano> entorno;
	private List<String> serviciosPersonalizados;

	public List<String> getServiciosPersonalizados() {
		return serviciosPersonalizados;
	}

	public void setServiciosPersonalizados(List<String> serviciosPersonalizados) {
		this.serviciosPersonalizados = serviciosPersonalizados;
	}
	private Long idPerfilArrendador;
	private String arrendadorNombre;

	public Long getIdPerfilArrendador() {
		return idPerfilArrendador;
	}

	public void setIdPerfilArrendador(Long idPerfilArrendador) {
		this.idPerfilArrendador = idPerfilArrendador;
	}
	private List<String> imagenes = List.of();

	public String getArrendadorNombre() {
		return arrendadorNombre;
	}

	public void setArrendadorNombre(String nombre) {
		this.arrendadorNombre = nombre;
	}

	public List<String> getImagenes() {
		return imagenes;
	}

	public void setImagenes(List<String> imagenes) {
		this.imagenes = imagenes;
	}
	private Double calificacion;
	private long totalResenas;

	public PropiedadResponseDto() {
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

	public BigDecimal getArea() {
		return area;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
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
	public Double getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(Double calificacion) {
		this.calificacion = calificacion;
	}

	public long getTotalResenas() {
		return totalResenas;
	}

	public void setTotalResenas(long totalResenas) {
		this.totalResenas = totalResenas;
	}

	public List<LugarCercano> getEntorno() {
		return entorno;
	}

	public void setEntorno(List<LugarCercano> entorno) {
		this.entorno = entorno;
	}
}
