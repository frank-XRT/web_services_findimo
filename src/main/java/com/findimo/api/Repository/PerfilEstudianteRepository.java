package com.findimo.api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.findimo.api.Entity.PerfilEstudianteEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilEstudianteRepository extends JpaRepository<PerfilEstudianteEntity, Long> {
}
