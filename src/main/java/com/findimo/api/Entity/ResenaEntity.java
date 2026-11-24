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
@Table(name = "tt_resena", schema = "findimo")
public class ResenaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDRESENA")
    private Long idResena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDUSUARIOAUTOR", nullable = false)
    private UsuarioEntity usuarioAutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDPROPIEDAD")
    private PropiedadEntity propiedad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDUSUARIOOBJETIVO")
    private UsuarioEntity usuarioObjetivo;

    @Column(name = "CALIFICACION", nullable = false)
    private Integer calificacion;

    @Column(name = "COMENTARIO")
    private String comentario;

    @Column(name = "ESTADO", nullable = false)
    private Boolean estado;

    @Column(name = "FECHACREACION", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public ResenaEntity() {
    }

    public Long getIdResena() {
        return idResena;
    }

    public void setIdResena(Long idResena) {
        this.idResena = idResena;
    }

    public UsuarioEntity getUsuarioAutor() {
        return usuarioAutor;
    }

    public void setUsuarioAutor(UsuarioEntity usuarioAutor) {
        this.usuarioAutor = usuarioAutor;
    }

    public PropiedadEntity getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(PropiedadEntity propiedad) {
        this.propiedad = propiedad;
    }

    public UsuarioEntity getUsuarioObjetivo() {
        return usuarioObjetivo;
    }

    public void setUsuarioObjetivo(UsuarioEntity usuarioObjetivo) {
        this.usuarioObjetivo = usuarioObjetivo;
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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}