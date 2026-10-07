package com.findimo.api.Dto;

import java.math.BigDecimal;

public class RegistroUsuarioResponseDto {

    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String correoElectronico;
    private String telefono;
    private String nombreRol;
    private Long idPerfilEstudiante;
    private Long idPerfilArrendador;
    private String nombreUniversidad;
    private BigDecimal presupuestoMaximo;
    private String cuentaBancaria;
    private String mensaje;

    public RegistroUsuarioResponseDto() {
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public Long getIdPerfilEstudiante() {
        return idPerfilEstudiante;
    }

    public void setIdPerfilEstudiante(Long idPerfilEstudiante) {
        this.idPerfilEstudiante = idPerfilEstudiante;
    }

    public Long getIdPerfilArrendador() {
        return idPerfilArrendador;
    }

    public void setIdPerfilArrendador(Long idPerfilArrendador) {
        this.idPerfilArrendador = idPerfilArrendador;
    }

    public String getNombreUniversidad() {
        return nombreUniversidad;
    }

    public void setNombreUniversidad(String nombreUniversidad) {
        this.nombreUniversidad = nombreUniversidad;
    }

    public BigDecimal getPresupuestoMaximo() {
        return presupuestoMaximo;
    }

    public void setPresupuestoMaximo(BigDecimal presupuestoMaximo) {
        this.presupuestoMaximo = presupuestoMaximo;
    }

    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}