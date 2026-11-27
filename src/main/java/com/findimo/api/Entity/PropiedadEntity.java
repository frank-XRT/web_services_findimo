package com.findimo.api.Entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
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
	@Column(name = "area_m2", precision = 8, scale = 2)
	private BigDecimal area;

	@Column(name = "DIRECCION")
	private String direccion;

	@Column(name = "DISTRITO")
	private String distrito;

	@Column(name = "ESTADO")
	private Boolean estado;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "entorno", columnDefinition = "jsonb")
	private List<LugarCercano> entorno;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "servicios_personalizados", columnDefinition = "jsonb")
	private List<String> serviciosPersonalizados = List.of();

	public List<String> getServiciosPersonalizados() {
		return serviciosPersonalizados;
	}

	public void setServiciosPersonalizados(List<String> serviciosPersonalizados) {
		this.serviciosPersonalizados = serviciosPersonalizados;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPerfilArrendador", referencedColumnName = "idPerfilArrendador")
	private PerfilArrendadorEntity perfilArrendador;

	@Column(name = "idtipopropiedad")
	private Integer idTipoPropiedad;

	@ElementCollection
	@CollectionTable(name = "tt_propiedad_servicio", schema = "findimo", joinColumns = @JoinColumn(name = "idpropiedad"))
	@Column(name = "idservicio", nullable = false)
	private Set<Integer> idsServicios = new HashSet<>();

	public Integer getIdTipoPropiedad() {
		return idTipoPropiedad;
	}

	public void setIdTipoPropiedad(Integer idTipoPropiedad) {
		this.idTipoPropiedad = idTipoPropiedad;
	}

	public Set<Integer> getIdsServicios() {
		return idsServicios;
	}

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

	public PerfilArrendadorEntity getPerfilArrendador() {
		return perfilArrendador;
	}

	public void setPerfilArrendador(PerfilArrendadorEntity perfilArrendador) {
		this.perfilArrendador = perfilArrendador;
	}

	public List<LugarCercano> getEntorno() {
		return entorno;
	}

	public void setEntorno(List<LugarCercano> entorno) {
		this.entorno = entorno;
	}
}
