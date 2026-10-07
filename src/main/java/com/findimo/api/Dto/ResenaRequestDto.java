package com.findimo.api.Dto;

public class ResenaRequestDto {

    private Long idUsuarioAutor;
    private Long idPropiedad;
    private Long idUsuarioObjetivo;
    private Integer calificacion;
    private String comentario;
    private Boolean estado;

    public ResenaRequestDto() {
    }

    public Long getIdUsuarioAutor() {
        return idUsuarioAutor;
    }

    public void setIdUsuarioAutor(Long idUsuarioAutor) {
        this.idUsuarioAutor = idUsuarioAutor;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}