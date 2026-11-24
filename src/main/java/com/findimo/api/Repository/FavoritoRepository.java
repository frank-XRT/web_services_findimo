package com.findimo.api.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.findimo.api.Entity.FavoritoEntity;
import com.findimo.api.Entity.PerfilEstudianteEntity;
import com.findimo.api.Entity.PropiedadEntity;

@Repository
public interface FavoritoRepository extends JpaRepository<FavoritoEntity, Long> {

    FavoritoEntity findByPerfilEstudianteAndPropiedad(
            PerfilEstudianteEntity perfilEstudiante,
            PropiedadEntity propiedad
    );

    List<FavoritoEntity> findByPerfilEstudianteAndEstadoTrue(
            PerfilEstudianteEntity perfilEstudiante
    );
}