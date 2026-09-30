package com.findimo.api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.findimo.api.Entity.UsuarioEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
}
