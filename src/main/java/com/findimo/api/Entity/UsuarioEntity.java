package com.findimo.api.Entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
@Table(name = "tm_usuario", schema = "findimo")
public class UsuarioEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDUSUARIO")
	private Long idUsuario;

	@Column(name = "CORREOELECTRONICO")
	private String correoElectronico;

	@Column(name = "CONTRASENA")
	private String contrasena;

	@Column(name = "NOMBRE")
	private String nombre;

	@Column(name = "APELLIDO")
	private String apellido;

	@Column(name = "TELEFONO")
	private String telefono;

	@Column(name = "ESTADO", nullable = false)
	private boolean estado = true;

	public boolean isEstado() {
		return estado;
	}

	@Column(name = "FECHACREACION", insertable = false, updatable = false)
	private LocalDateTime fechaCreacion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idRol", referencedColumnName = "idRol")
	private RolEntity rol;

	@ElementCollection
	@CollectionTable(name = "tt_favorito_usuario", schema = "findimo", joinColumns = @JoinColumn(name = "idusuario"))
	@Column(name = "idpropiedad", nullable = false)
	private Set<Long> propiedadesFavoritas = new HashSet<>();

	public Set<Long> getPropiedadesFavoritas() {
		return propiedadesFavoritas;
	}

	public UsuarioEntity() {
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public RolEntity getRol() {
		return rol;
	}

	public void setRol(RolEntity rol) {
		this.rol = rol;
	}
}
