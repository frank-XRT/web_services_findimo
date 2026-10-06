package com.findimo.api.Service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.findimo.api.Dto.SolicitudAlquilerRequestDto;
import com.findimo.api.Dto.SolicitudAlquilerResponseDto;
import com.findimo.api.Entity.PerfilEstudianteEntity;
import com.findimo.api.Entity.PropiedadEntity;
import com.findimo.api.Entity.SolicitudAlquilerEntity;
import com.findimo.api.Repository.PerfilEstudianteRepository;
import com.findimo.api.Repository.PropiedadRepository;
import com.findimo.api.Repository.SolicitudAlquilerRepository;
import com.findimo.api.Service.SolicitudAlquilerService;

@Service
public class SolicitudAlquilerServiceImpl
        implements SolicitudAlquilerService {

    @Autowired
    private SolicitudAlquilerRepository solicitudAlquilerRepository;

    @Autowired
    private PerfilEstudianteRepository perfilEstudianteRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;


    @Override
    public SolicitudAlquilerResponseDto registrarSolicitud(
            SolicitudAlquilerRequestDto dto) {

        // Validar perfil estudiante
        if (dto.getIdPerfilEstudiante() == null) {

            throw new RuntimeException(
                    "El id del perfil estudiante es obligatorio."
            );
        }


        // Validar propiedad
        if (dto.getIdPropiedad() == null) {

            throw new RuntimeException(
                    "El id de la propiedad es obligatorio."
            );
        }


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


        // Crear solicitud
        SolicitudAlquilerEntity solicitud =
                new SolicitudAlquilerEntity();

        solicitud.setPerfilEstudiante(
                perfilEstudiante
        );

        solicitud.setPropiedad(
                propiedad
        );

        // Estado inicial
        solicitud.setEstado(true);


        // Guardar solicitud
        SolicitudAlquilerEntity solicitudGuardada =
                solicitudAlquilerRepository.save(
                        solicitud
                );


        // Crear respuesta
        SolicitudAlquilerResponseDto response =
                new SolicitudAlquilerResponseDto();

        response.setIdSolicitudAlquiler(
                solicitudGuardada.getIdSolicitudAlquiler()
        );

        response.setIdPerfilEstudiante(
                solicitudGuardada
                        .getPerfilEstudiante()
                        .getIdPerfilEstudiante()
        );

        response.setIdPropiedad(
                solicitudGuardada
                        .getPropiedad()
                        .getIdPropiedad()
        );

        response.setEstado(
                solicitudGuardada.getEstado()
        );

        response.setFechaCreacion(
                solicitudGuardada.getFechaCreacion()
        );


        return response;
    }
}