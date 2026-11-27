package com.findimo.api.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.findimo.api.Entity.ImagenPropiedadEntity;

@Repository
public interface ImagenPropiedadRepository extends JpaRepository<ImagenPropiedadEntity, Long> {
	List<ImagenPropiedadEntity> findByEstadoTrueOrderByPrincipalDescIdImagenPropiedadAsc();
	List<ImagenPropiedadEntity> findByPropiedad_IdPropiedad(Long idPropiedad);
	List<ImagenPropiedadEntity> findByPropiedad_IdPropiedadAndEstadoTrue(Long idPropiedad);
	List<ImagenPropiedadEntity> findByPropiedad_IdPropiedadAndEstadoTrueOrderByPrincipalDescIdImagenPropiedadAsc(Long idPropiedad);
}
