package com.findimo.api.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.findimo.api.Entity.FavoritoEntity;

@Repository
public interface FavoritoRepository extends JpaRepository<FavoritoEntity, Long> {

    Optional<FavoritoEntity> findByPerfilEstudiante_IdPerfilEstudianteAndPropiedad_IdPropiedad(
            Long idPerfilEstudiante,
            Long idPropiedad
    );

    List<FavoritoEntity> findByPerfilEstudiante_IdPerfilEstudianteAndEstadoTrue(
            Long idPerfilEstudiante
    );
}