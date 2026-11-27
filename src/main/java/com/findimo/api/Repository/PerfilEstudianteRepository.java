package com.findimo.api.Repository;

import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import com.findimo.api.Entity.PerfilEstudianteEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilEstudianteRepository extends JpaRepository<PerfilEstudianteEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PerfilEstudianteEntity> findByIdPerfilEstudiante(Long idPerfilEstudiante);

    boolean existsByIdPerfilEstudianteAndUsuario_IdUsuario(Long idPerfilEstudiante, Long idUsuario);

	Optional<PerfilEstudianteEntity> findByUsuario_IdUsuario(Long idUsuario);
}
