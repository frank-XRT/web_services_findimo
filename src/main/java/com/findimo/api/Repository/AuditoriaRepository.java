package com.findimo.api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.findimo.api.Entity.AuditoriaEntity;

@Repository
public interface AuditoriaRepository
        extends JpaRepository<AuditoriaEntity, Long> {

}