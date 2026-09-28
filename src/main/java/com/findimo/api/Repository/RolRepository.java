package com.findimo.api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.findimo.api.Entity.RolEntity;

public interface RolRepository extends JpaRepository<RolEntity, Integer> {
}
