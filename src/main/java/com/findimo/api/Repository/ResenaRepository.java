package com.findimo.api.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.findimo.api.Entity.ResenaEntity;

@Repository
public interface ResenaRepository extends JpaRepository<ResenaEntity, Long> {

    boolean existsBySolicitudAlquiler_IdSolicitudAlquilerAndUsuarioAutor_IdUsuarioAndUsuarioObjetivoIsNull(Long idSolicitud, Long idAutor);

    boolean existsBySolicitudAlquiler_IdSolicitudAlquilerAndUsuarioAutor_IdUsuarioAndUsuarioObjetivoIsNotNull(Long idSolicitud, Long idAutor);

    interface ValoracionPropiedad {
        Long getIdPropiedad();
        Double getCalificacion();
        Long getTotalResenas();
    }

    @Query("""
            select r.propiedad.idPropiedad as idPropiedad, avg(r.calificacion) as calificacion, count(r) as totalResenas
            from ResenaEntity r
            where r.estado = true and r.propiedad is not null and r.usuarioObjetivo is null
            group by r.propiedad.idPropiedad
            """)
    List<ValoracionPropiedad> resumirResenasPorPropiedad();

    List<ResenaEntity> findByPropiedad_IdPropiedadAndUsuarioObjetivoIsNullAndEstadoTrueOrderByIdResenaDesc(Long idPropiedad);

    @Query("""
            select r from ResenaEntity r
            where r.estado = true and r.usuarioObjetivo.idUsuario = :idUsuario
                and r.propiedad.perfilArrendador.usuario.idUsuario = :idUsuario
            order by r.idResena desc
            """)
    List<ResenaEntity> listarResenasDelArrendador(Long idUsuario);

    List<ResenaEntity> findByUsuarioObjetivo_IdUsuarioAndEstadoTrueOrderByIdResenaDesc(Long idUsuario);

    List<ResenaEntity> findTop10ByUsuarioObjetivo_IdUsuarioAndEstadoTrueOrderByIdResenaDesc(Long idUsuario);

    List<ResenaEntity> findByUsuarioAutor_IdUsuarioAndEstadoTrueOrderByIdResenaDesc(Long idUsuario);
}