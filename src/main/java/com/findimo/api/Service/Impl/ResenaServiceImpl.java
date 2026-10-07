package com.findimo.api.Service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.findimo.api.Dto.ResenaRequestDto;
import com.findimo.api.Dto.ResenaResponseDto;
import com.findimo.api.Entity.PropiedadEntity;
import com.findimo.api.Entity.ResenaEntity;
import com.findimo.api.Entity.UsuarioEntity;
import com.findimo.api.Repository.PropiedadRepository;
import com.findimo.api.Repository.ResenaRepository;
import com.findimo.api.Repository.UsuarioRepository;
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

        List<ResenaEntity> resenas = resenaRepository.findByPropiedad_IdPropiedad(idPropiedad);
        List<ResenaResponseDto> response = new ArrayList<>();

        for (ResenaEntity resena : resenas) {

            ResenaResponseDto dto = new ResenaResponseDto();

            dto.setIdResena(resena.getIdResena());
            dto.setIdUsuarioAutor(resena.getUsuarioAutor().getIdUsuario());
            dto.setNombreAutor(resena.getUsuarioAutor().getNombre());
            dto.setApellidoAutor(resena.getUsuarioAutor().getApellido());
            dto.setIdPropiedad(resena.getPropiedad().getIdPropiedad());
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

        UsuarioEntity usuarioAutor = usuarioRepository.getReferenceById(dto.getIdUsuarioAutor());

        PropiedadEntity propiedad = null;

        if (dto.getIdPropiedad() != null) {
            propiedad = propiedadRepository.getReferenceById(dto.getIdPropiedad());
        }

        UsuarioEntity usuarioObjetivo = null;

        if (dto.getIdUsuarioObjetivo() != null) {
            usuarioObjetivo = usuarioRepository.getReferenceById(dto.getIdUsuarioObjetivo());
        }

        ResenaEntity resena = new ResenaEntity();
        resena.setUsuarioAutor(usuarioAutor);
        resena.setPropiedad(propiedad);
        resena.setUsuarioObjetivo(usuarioObjetivo);
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        resena.setEstado(true);

        ResenaEntity resenaGuardada = resenaRepository.save(resena);

        ResenaResponseDto response = new ResenaResponseDto();
        response.setIdResena(resenaGuardada.getIdResena());
        response.setIdUsuarioAutor(usuarioAutor.getIdUsuario());
        response.setNombreAutor(usuarioAutor.getNombre());
        response.setApellidoAutor(usuarioAutor.getApellido());

        if (propiedad != null) {
            response.setIdPropiedad(propiedad.getIdPropiedad());
        }

        if (usuarioObjetivo != null) {
            response.setIdUsuarioObjetivo(usuarioObjetivo.getIdUsuario());
        }

        response.setCalificacion(resenaGuardada.getCalificacion());
        response.setComentario(resenaGuardada.getComentario());
        response.setFechaCreacion(resenaGuardada.getFechaCreacion());
        response.setEstado(resenaGuardada.getEstado());

        return response;
    }
}