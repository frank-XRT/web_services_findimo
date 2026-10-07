package com.findimo.api.Service.Impl;

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
public class SolicitudAlquilerServiceImpl implements SolicitudAlquilerService {

    @Autowired
    private SolicitudAlquilerRepository solicitudAlquilerRepository;

    @Autowired
    private PerfilEstudianteRepository perfilEstudianteRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Override
    public SolicitudAlquilerResponseDto registrarSolicitud(SolicitudAlquilerRequestDto dto) {

        PerfilEstudianteEntity perfilEstudiante = perfilEstudianteRepository.findById(dto.getIdPerfilEstudiante()).orElse(null);

        PropiedadEntity propiedad = propiedadRepository.findById(dto.getIdPropiedad()).orElse(null);

        SolicitudAlquilerEntity solicitud = new SolicitudAlquilerEntity();
        solicitud.setPerfilEstudiante(perfilEstudiante);
        solicitud.setPropiedad(propiedad);
        solicitud.setEstado(true);

        SolicitudAlquilerEntity solicitudGuardada = solicitudAlquilerRepository.save(solicitud);

        SolicitudAlquilerResponseDto response = new SolicitudAlquilerResponseDto();
        response.setIdSolicitudAlquiler(solicitudGuardada.getIdSolicitudAlquiler());
        response.setIdPerfilEstudiante(solicitudGuardada.getPerfilEstudiante().getIdPerfilEstudiante());
        response.setIdPropiedad(solicitudGuardada.getPropiedad().getIdPropiedad());
        response.setEstado(solicitudGuardada.getEstado());
        response.setFechaCreacion(solicitudGuardada.getFechaCreacion());

        return response;
    }
}