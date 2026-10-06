package com.findimo.api.Service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.findimo.api.Dto.ResenaRequestDto;
import com.findimo.api.Entity.PropiedadEntity;
import com.findimo.api.Entity.UsuarioEntity;
import com.findimo.api.Repository.PropiedadRepository;
import com.findimo.api.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.findimo.api.Dto.ResenaResponseDto;
import com.findimo.api.Entity.ResenaEntity;
import com.findimo.api.Repository.ResenaRepository;
import com.findimo.api.Service.ResenaService;

@Service
public class ResenaServiceImpl implements ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Override
    public List<ResenaResponseDto> listarResenasPorPropiedad(Long idPropiedad) {

        List<ResenaEntity> resenas =
                resenaRepository.findByPropiedad_IdPropiedad(idPropiedad);

        List<ResenaResponseDto> response = new ArrayList<>();

        for (ResenaEntity resena : resenas) {

            ResenaResponseDto dto = new ResenaResponseDto();

            dto.setIdResena(resena.getIdResena());

            if (resena.getUsuarioAutor() != null) {
                dto.setIdUsuarioAutor(
                        resena.getUsuarioAutor().getIdUsuario()
                );

                dto.setNombreAutor(
                        resena.getUsuarioAutor().getNombre()
                );

                dto.setApellidoAutor(
                        resena.getUsuarioAutor().getApellido()
                );
            }

            if (resena.getPropiedad() != null) {
                dto.setIdPropiedad(
                        resena.getPropiedad().getIdPropiedad()
                );
            }

            dto.setCalificacion(resena.getCalificacion());
            dto.setComentario(resena.getComentario());
            dto.setFechaCreacion(resena.getFechaCreacion());
            dto.setEstado(resena.getEstado());

            response.add(dto);
        }

        return response;
    }

    @Override
    public ResenaResponseDto registrarResena(ResenaRequestDto dto) {
        // Validar que exista al menos una propiedad o un usuario objetivo
        if (dto.getIdPropiedad() == null
                && dto.getIdUsuarioObjetivo() == null) {

            throw new RuntimeException(
                    "La reseña debe estar relacionada con una propiedad o con un usuario."
            );
        }

        // Validar calificación
        if (dto.getCalificacion() == null
                || dto.getCalificacion() < 1
                || dto.getCalificacion() > 5) {

            throw new RuntimeException(
                    "La calificación debe estar entre 1 y 5."
            );
        }

        // Buscar usuario autor
        Optional<UsuarioEntity> usuarioAutorOptional =
                usuarioRepository.findById(dto.getIdUsuarioAutor());

        if (usuarioAutorOptional.isEmpty()) {
            throw new RuntimeException(
                    "El usuario autor no existe."
            );
        }

        UsuarioEntity usuarioAutor =
                usuarioAutorOptional.get();

        // Buscar propiedad si fue enviada
        PropiedadEntity propiedad = null;

        if (dto.getIdPropiedad() != null) {

            Optional<PropiedadEntity> propiedadOptional =
                    propiedadRepository.findById(dto.getIdPropiedad());

            if (propiedadOptional.isEmpty()) {
                throw new RuntimeException(
                        "La propiedad no existe."
                );
            }

            propiedad = propiedadOptional.get();
        }

        // Buscar usuario objetivo si fue enviado
        UsuarioEntity usuarioObjetivo = null;

        if (dto.getIdUsuarioObjetivo() != null) {

            Optional<UsuarioEntity> usuarioObjetivoOptional =
                    usuarioRepository.findById(dto.getIdUsuarioObjetivo());

            if (usuarioObjetivoOptional.isEmpty()) {
                throw new RuntimeException(
                        "El usuario objetivo no existe."
                );
            }

            usuarioObjetivo = usuarioObjetivoOptional.get();
        }

        // Crear reseña
        ResenaEntity resena = new ResenaEntity();

        resena.setUsuarioAutor(usuarioAutor);
        resena.setPropiedad(propiedad);
        resena.setUsuarioObjetivo(usuarioObjetivo);
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        resena.setEstado(true); // ← FALTABA ESTO

        ResenaEntity resenaGuardada =
                resenaRepository.save(resena);

        // Crear respuesta
        ResenaResponseDto response =
                new ResenaResponseDto();

        response.setIdResena(
                resenaGuardada.getIdResena()
        );

        response.setIdUsuarioAutor(
                usuarioAutor.getIdUsuario()
        );

        response.setNombreAutor(
                usuarioAutor.getNombre()
        );

        response.setApellidoAutor(
                usuarioAutor.getApellido()
        );

        if (propiedad != null) {
            response.setIdPropiedad(
                    propiedad.getIdPropiedad()
            );
        }

        if (usuarioObjetivo != null) {
            response.setIdUsuarioObjetivo(
                    usuarioObjetivo.getIdUsuario()
            );
        }

        response.setCalificacion(
                resenaGuardada.getCalificacion()
        );

        response.setComentario(
                resenaGuardada.getComentario()
        );

        response.setFechaCreacion(
                resenaGuardada.getFechaCreacion()
        );

        response.setEstado(
                resenaGuardada.getEstado()
        );

        return response;
    }
}