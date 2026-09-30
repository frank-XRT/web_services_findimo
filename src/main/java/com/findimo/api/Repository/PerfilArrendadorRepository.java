package com.findimo.api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.findimo.api.Entity.PerfilArrendadorEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilArrendadorRepository extends JpaRepository<PerfilArrendadorEntity, Long> {}
