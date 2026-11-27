package com.findimo.api.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.findimo.api.Entity.MensajeAlquilerEntity;

public interface MensajeAlquilerRepository extends JpaRepository<MensajeAlquilerEntity, Long> {
    List<MensajeAlquilerEntity> findTop100BySolicitud_IdSolicitudAlquilerOrderByIdMensajeDesc(Long idSolicitud);
}
