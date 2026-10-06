package com.findimo.api.Service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.findimo.api.Dto.FavoritoRequestDto;
import com.findimo.api.Dto.FavoritoResponseDto;
import com.findimo.api.Entity.FavoritoEntity;
import com.findimo.api.Entity.PerfilEstudianteEntity;
import com.findimo.api.Entity.PropiedadEntity;
import com.findimo.api.Repository.FavoritoRepository;
import com.findimo.api.Repository.PerfilEstudianteRepository;
import com.findimo.api.Repository.PropiedadRepository;
import com.findimo.api.Service.AuditoriaService;
import com.findimo.api.Service.FavoritoService;

@Service
public class FavoritoServiceImpl implements FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private PerfilEstudianteRepository perfilEstudianteRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private AuditoriaService auditoriaService;


    @Override
    public FavoritoResponseDto registrarFavorito(
            FavoritoRequestDto dto) {

        // Buscar perfil estudiante
        Optional<PerfilEstudianteEntity> perfilOptional =
                perfilEstudianteRepository.findById(
                        dto.getIdPerfilEstudiante()
                );

        if (perfilOptional.isEmpty()) {
            throw new RuntimeException(
                    "El perfil estudiante no existe."
            );
        }

        PerfilEstudianteEntity perfilEstudiante =
                perfilOptional.get();


        // Buscar propiedad
        Optional<PropiedadEntity> propiedadOptional =
                propiedadRepository.findById(
                        dto.getIdPropiedad()
                );

        if (propiedadOptional.isEmpty()) {
            throw new RuntimeException(
                    "La propiedad no existe."
            );
        }

        PropiedadEntity propiedad =
                propiedadOptional.get();


        // Verificar si ya existe
        Optional<FavoritoEntity> favoritoOptional =
                favoritoRepository
                        .findByPerfilEstudiante_IdPerfilEstudianteAndPropiedad_IdPropiedad(
                                dto.getIdPerfilEstudiante(),
                                dto.getIdPropiedad()
                        );


        FavoritoEntity favorito;

        if (favoritoOptional.isPresent()) {

            favorito = favoritoOptional.get();

            // Guardamos cómo estaba antes
            Map<String, Object> datosAnteriores =
                    crearDatosFavorito(favorito);

            // Si ya estaba activo
            if (Boolean.TRUE.equals(favorito.getEstado())) {

                // No hacemos nada
                return convertirRespuesta(favorito);
            }

            // Si estaba en false, lo reactivamos
            favorito.setEstado(true);

            FavoritoEntity favoritoGuardado =
                    favoritoRepository.save(favorito);

            Map<String, Object> datosNuevos =
                    crearDatosFavorito(favoritoGuardado);

            datosNuevos.put(
                    "accion",
                    "REACTIVAR"
            );

            auditoriaService.registrarAuditoria(
                    dto.getIdUsuario(),
                    null,
                    datosAnteriores,
                    datosNuevos
            );

            return convertirRespuesta(favoritoGuardado);

        } else {

            // Crear nuevo favorito
            favorito = new FavoritoEntity();

            favorito.setPerfilEstudiante(
                    perfilEstudiante
            );

            favorito.setPropiedad(
                    propiedad
            );

            favorito.setEstado(true);

            FavoritoEntity favoritoGuardado =
                    favoritoRepository.save(favorito);


            // Datos nuevos
            Map<String, Object> datosNuevos =
                    crearDatosFavorito(favoritoGuardado);

            datosNuevos.put(
                    "accion",
                    "CREAR"
            );


            // Registrar auditoría
            auditoriaService.registrarAuditoria(
                    dto.getIdUsuario(),
                    null,
                    null,
                    datosNuevos
            );

            return convertirRespuesta(favoritoGuardado);
        }
    }


    @Override
    public FavoritoResponseDto cambiarEstadoFavorito(
            Long idFavorito,
            Boolean estado,
            Long idUsuario) {

        // Buscar favorito
        Optional<FavoritoEntity> favoritoOptional =
                favoritoRepository.findById(idFavorito);

        if (favoritoOptional.isEmpty()) {
            throw new RuntimeException(
                    "El favorito no existe."
            );
        }

        FavoritoEntity favorito =
                favoritoOptional.get();


        // Guardar estado anterior
        Map<String, Object> datosAnteriores =
                crearDatosFavorito(favorito);


        // Cambiar estado
        favorito.setEstado(estado);

        FavoritoEntity favoritoGuardado =
                favoritoRepository.save(favorito);


        // Datos nuevos
        Map<String, Object> datosNuevos =
                crearDatosFavorito(favoritoGuardado);


        if (Boolean.FALSE.equals(estado)) {

            datosNuevos.put(
                    "accion",
                    "DESACTIVAR"
            );

            auditoriaService.registrarAuditoria(
                    idUsuario,
                    idUsuario,
                    datosAnteriores,
                    datosNuevos
            );

        } else {

            datosNuevos.put(
                    "accion",
                    "ACTIVAR"
            );

            auditoriaService.registrarAuditoria(
                    idUsuario,
                    null,
                    datosAnteriores,
                    datosNuevos
            );
        }


        return convertirRespuesta(favoritoGuardado);
    }


    @Override
    public List<FavoritoResponseDto>
    listarFavoritosPorPerfilEstudiante(
            Long idPerfilEstudiante) {

        List<FavoritoEntity> favoritos =
                favoritoRepository
                        .findByPerfilEstudiante_IdPerfilEstudianteAndEstadoTrue(
                                idPerfilEstudiante
                        );

        List<FavoritoResponseDto> response =
                new ArrayList<>();


        for (FavoritoEntity favorito : favoritos) {

            response.add(
                    convertirRespuesta(favorito)
            );
        }

        return response;
    }


    private Map<String, Object> crearDatosFavorito(
            FavoritoEntity favorito) {

        Map<String, Object> datos =
                new HashMap<>();

        datos.put(
                "tabla",
                "tt_favorito"
        );

        datos.put(
                "idFavorito",
                favorito.getIdFavorito()
        );

        datos.put(
                "idPerfilEstudiante",
                favorito.getPerfilEstudiante()
                        .getIdPerfilEstudiante()
        );

        datos.put(
                "idPropiedad",
                favorito.getPropiedad()
                        .getIdPropiedad()
        );

        datos.put(
                "estado",
                favorito.getEstado()
        );

        return datos;
    }


    private FavoritoResponseDto convertirRespuesta(
            FavoritoEntity favorito) {

        FavoritoResponseDto response =
                new FavoritoResponseDto();

        response.setIdFavorito(
                favorito.getIdFavorito()
        );

        response.setIdPerfilEstudiante(
                favorito.getPerfilEstudiante()
                        .getIdPerfilEstudiante()
        );

        response.setIdPropiedad(
                favorito.getPropiedad()
                        .getIdPropiedad()
        );

        response.setEstado(
                favorito.getEstado()
        );

        return response;
    }
}