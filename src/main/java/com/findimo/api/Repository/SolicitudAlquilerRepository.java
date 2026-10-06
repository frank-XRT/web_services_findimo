package com.findimo.api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.findimo.api.Entity.SolicitudAlquilerEntity;

@Repository
public interface SolicitudAlquilerRepository
        extends JpaRepository<SolicitudAlquilerEntity, Long> {
}