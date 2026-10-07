package com.findimo.api.Dto;

import java.time.LocalDateTime;

public class ResenaResponseDto {

    private Long idResena;
    private Long idUsuarioAutor;
    private String nombreAutor;
    private String apellidoAutor;
    private Long idPropiedad;
    private Long idUsuarioObjetivo;
    private Integer calificacion;
    private String comentario;
    private LocalDateTime fechaCreacion;
    private Boolean estado;

    public ResenaResponseDto() {
    }

    public Long getIdResena() {
        return idResena;
    }

    public void setIdResena(Long idResena) {
        this.idResena = idResena;
    }

    public Long getIdUsuarioAutor() {
        return idUsuarioAutor;
    }

    public void setIdUsuarioAutor(Long idUsuarioAutor) {
        this.idUsuarioAutor = idUsuarioAutor;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public String getApellidoAutor() {
        return apellidoAutor;
    }

    public void setApellidoAutor(String apellidoAutor) {
        this.apellidoAutor = apellidoAutor;
    }

    public Long getIdPropiedad() {
        return idPropiedad;
    }

    public void setIdPropiedad(Long idPropiedad) {
        this.idPropiedad = idPropiedad;
    }

    public Long getIdUsuarioObjetivo() {
        return idUsuarioObjetivo;
    }

    public void setIdUsuarioObjetivo(Long idUsuarioObjetivo) {
        this.idUsuarioObjetivo = idUsuarioObjetivo;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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