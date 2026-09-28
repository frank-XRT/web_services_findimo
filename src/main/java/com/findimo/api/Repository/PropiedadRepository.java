package com.findimo.api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.findimo.api.Entity.PropiedadEntity;

public interface PropiedadRepository extends JpaRepository<PropiedadEntity, Long> {
}
