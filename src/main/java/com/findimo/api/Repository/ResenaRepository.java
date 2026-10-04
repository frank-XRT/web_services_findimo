package com.findimo.api.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.findimo.api.Entity.ResenaEntity;

@Repository
public interface ResenaRepository extends JpaRepository<ResenaEntity, Long> {

    List<ResenaEntity> findByPropiedad_IdPropiedad(Long idPropiedad);
}