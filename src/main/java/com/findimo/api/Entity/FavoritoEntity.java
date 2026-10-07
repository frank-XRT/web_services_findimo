package com.findimo.api.Entity;

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
@Table(name = "tt_favorito", schema = "findimo")
public class FavoritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDFAVORITO")
    private Long idFavorito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDPERFILESTUDIANTE", nullable = false)
    private PerfilEstudianteEntity perfilEstudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDPROPIEDAD", nullable = false)
    private PropiedadEntity propiedad;

    @Column(name = "ESTADO", nullable = false)
    private Boolean estado;

    public FavoritoEntity() {
    }

    public Long getIdFavorito() {
        return idFavorito;
    }

    public void setIdFavorito(Long idFavorito) {
        this.idFavorito = idFavorito;
    }

    public PerfilEstudianteEntity getPerfilEstudiante() {
        return perfilEstudiante;
    }

    public void setPerfilEstudiante(PerfilEstudianteEntity perfilEstudiante) {
        this.perfilEstudiante = perfilEstudiante;
    }

    public PropiedadEntity getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(PropiedadEntity propiedad) {
        this.propiedad = propiedad;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}