package com.findimo.api.Repository;

import com.findimo.api.Entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Integer> {
	RolEntity findByNombreRol(String nombreRol);
}
