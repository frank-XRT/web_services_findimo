package com.findimo.api.Dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.findimo.api.Entity.LugarCercano;

public class PropiedadRequestDto {

	private Integer idTipoPropiedad;
	private Set<Integer> idsServicios;
	private boolean tipoEnviado;

	public Integer getIdTipoPropiedad() {
		return idTipoPropiedad;
	}

	public void setIdTipoPropiedad(Integer idTipoPropiedad) {
		this.idTipoPropiedad = idTipoPropiedad;
		this.tipoEnviado = true;
	}

	public Set<Integer> getIdsServicios() {
		return idsServicios;
	}

	public void setIdsServicios(Set<Integer> idsServicios) {
		this.idsServicios = idsServicios;
	}

	public boolean tieneTipo() {
		return tipoEnviado;
	}

	private Long idPerfilArrendador;
	private String titulo;
	private String descripcion;
	private BigDecimal precio;
	private Integer habitaciones;
	private BigDecimal area;
	private String direccion;
	private String distrito;
	private Boolean estado;
	private List<LugarCercano> entorno;
	private List<String> serviciosPersonalizados;

	public List<String> getServiciosPersonalizados() {
		return serviciosPersonalizados;
	}

	public void setServiciosPersonalizados(List<String> serviciosPersonalizados) {
		this.serviciosPersonalizados = serviciosPersonalizados;
	}
	private List<String> fotosRetiradas;

	public List<String> getFotosRetiradas() {
		return fotosRetiradas;
	}

	public void setFotosRetiradas(List<String> fotosRetiradas) {
		this.fotosRetiradas = fotosRetiradas;
	}

	public PropiedadRequestDto() {
	}

	public Long getIdPerfilArrendador() {
		return idPerfilArrendador;
	}

	public void setIdPerfilArrendador(Long idPerfilArrendador) {
		this.idPerfilArrendador = idPerfilArrendador;
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

	public List<LugarCercano> getEntorno() {
		return entorno;
	}

	public void setEntorno(List<LugarCercano> entorno) {
		this.entorno = entorno;
	}
}
