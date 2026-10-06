package com.findimo.api.Repository;

import com.findimo.api.Entity.AuditoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRepository extends JpaRepository<AuditoriaEntity, Long> {
}