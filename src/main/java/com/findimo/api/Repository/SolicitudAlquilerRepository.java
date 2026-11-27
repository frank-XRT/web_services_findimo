package com.findimo.api.Repository;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.findimo.api.Entity.SolicitudAlquilerEntity;
import com.findimo.api.Entity.SolicitudAlquilerEntity.Reserva;

@Repository
public interface SolicitudAlquilerRepository
        extends JpaRepository<SolicitudAlquilerEntity, Long> {

    boolean existsByPropiedad_IdPropiedadAndReserva(Long idPropiedad, Reserva reserva);

    @Query("select s.propiedad.idPropiedad from SolicitudAlquilerEntity s where s.reserva = :reserva")
    List<Long> idsPropiedadesPorReserva(@Param("reserva") Reserva reserva);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SolicitudAlquilerEntity> findByIdSolicitudAlquiler(Long idSolicitudAlquiler);

    @Query("""
            select s from SolicitudAlquilerEntity s
            where s.perfilEstudiante.idPerfilEstudiante = :estudiante and s.propiedad.idPropiedad = :propiedad
            and s.estado in :estados and (s.reserva is null or s.reserva <> :finalizada)
            order by s.idSolicitudAlquiler
            """)
    List<SolicitudAlquilerEntity> buscarSolicitudesVigentes(@Param("estudiante") Long estudiante,
            @Param("propiedad") Long propiedad, @Param("estados") List<String> estados,
            @Param("finalizada") Reserva finalizada);

    @Query("""
            select s from SolicitudAlquilerEntity s
            join fetch s.perfilEstudiante e join fetch e.usuario estudiante
            join fetch s.propiedad p
            join fetch p.perfilArrendador a join fetch a.usuario arrendador
            where estudiante.idUsuario = :cuenta or arrendador.idUsuario = :cuenta
            order by s.idSolicitudAlquiler desc
            """)
    List<SolicitudAlquilerEntity> listarConversaciones(@Param("cuenta") Long cuenta);

}