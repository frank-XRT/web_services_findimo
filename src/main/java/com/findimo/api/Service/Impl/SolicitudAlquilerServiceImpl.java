package com.findimo.api.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.findimo.api.Dto.SolicitudAlquilerRequestDto;
import com.findimo.api.Dto.SolicitudAlquilerResponseDto;
import com.findimo.api.Entity.PerfilEstudianteEntity;
import com.findimo.api.Entity.PropiedadEntity;
import com.findimo.api.Entity.SolicitudAlquilerEntity;
import com.findimo.api.Entity.SolicitudAlquilerEntity.Situacion;
import com.findimo.api.Entity.SolicitudAlquilerEntity.Reserva;
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
    @Transactional
    public SolicitudAlquilerResponseDto registrarSolicitud(SolicitudAlquilerRequestDto dto) {

        PerfilEstudianteEntity perfilEstudiante = perfilEstudianteRepository.findByIdPerfilEstudiante(dto.getIdPerfilEstudiante())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        PropiedadEntity propiedad = propiedadRepository.findByIdPropiedad(dto.getIdPropiedad())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var arrendador = propiedad.getPerfilArrendador();
        if (arrendador != null && arrendador.getUsuario() != null
                && arrendador.getUsuario().getIdUsuario().equals(perfilEstudiante.getUsuario().getIdUsuario())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes solicitar una vivienda propia.");
        }
        var existente = solicitudAlquilerRepository
                .buscarSolicitudesVigentes(
                        dto.getIdPerfilEstudiante(), dto.getIdPropiedad(), List.of("PENDIENTE", "ACEPTADA", "true"), Reserva.FINALIZADA).stream().findFirst();
        if (existente.isPresent()) {
            return convertir(existente.get());
        }
        if (propiedad.getPerfilArrendador() == null || propiedad.getPerfilArrendador().getUsuario() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La vivienda no tiene un arrendador asociado.");
        }
        if (!Boolean.TRUE.equals(propiedad.getEstado())
                || solicitudAlquilerRepository.existsByPropiedad_IdPropiedadAndReserva(dto.getIdPropiedad(), Reserva.CONFIRMADA)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La vivienda no está disponible.");
        }

        SolicitudAlquilerEntity solicitud = new SolicitudAlquilerEntity();
        solicitud.setPerfilEstudiante(perfilEstudiante);
        solicitud.setPropiedad(propiedad);

        return convertir(solicitudAlquilerRepository.save(solicitud));
    }

    private SolicitudAlquilerResponseDto convertir(SolicitudAlquilerEntity solicitudGuardada) {

        SolicitudAlquilerResponseDto response = new SolicitudAlquilerResponseDto();
        response.setIdSolicitudAlquiler(solicitudGuardada.getIdSolicitudAlquiler());
        response.setIdPerfilEstudiante(solicitudGuardada.getPerfilEstudiante().getIdPerfilEstudiante());
        response.setIdPropiedad(solicitudGuardada.getPropiedad().getIdPropiedad());
        response.setEstado(solicitudGuardada.getSituacion() != Situacion.CANCELADA);
        response.setFechaCreacion(solicitudGuardada.getFechaCreacion());

        return response;
    }
}