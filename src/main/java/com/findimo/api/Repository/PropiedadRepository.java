package com.findimo.api.Repository;

import java.util.Optional;
import java.util.List;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.findimo.api.Entity.PropiedadEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PropiedadRepository extends JpaRepository<PropiedadEntity, Long> {
    List<PropiedadEntity> findByEstadoTrue();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PropiedadEntity> findByIdPropiedad(Long propiedad);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PropiedadEntity> findByIdPropiedadAndPerfilArrendador_IdPerfilArrendador(Long propiedad, Long arrendador);
}
