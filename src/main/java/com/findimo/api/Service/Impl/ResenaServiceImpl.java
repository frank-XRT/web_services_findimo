package com.findimo.api.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.findimo.api.Dto.ResenaRequestDto;
import com.findimo.api.Dto.ResenaResponseDto;
import com.findimo.api.Dto.ResenasPerfilDto;
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
    @Transactional(readOnly = true)
    public List<ResenaResponseDto> listarResenasPorPropiedad(Long idPropiedad) {
        return resenaRepository.findByPropiedad_IdPropiedadAndUsuarioObjetivoIsNullAndEstadoTrueOrderByIdResenaDesc(idPropiedad)
                .stream().map(this::convertir).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResenaResponseDto> listarResenasDelArrendador(Long idPropiedad) {
        var propietario = propiedadRepository.findById(idPropiedad)
                .map(PropiedadEntity::getPerfilArrendador)
                .map(perfil -> perfil.getUsuario());
        return propietario.map(usuario -> resenaRepository
                .listarResenasDelArrendador(usuario.getIdUsuario())
                .stream().map(this::convertir).toList()).orElseGet(List::of);
    }

    @Override
    @Transactional(readOnly = true)
    public ResenasPerfilDto listarResenasDePerfil(Long idUsuario) {
        var recibidas = resenaRepository.findByUsuarioObjetivo_IdUsuarioAndEstadoTrueOrderByIdResenaDesc(idUsuario)
                .stream().map(this::convertir).toList();
        var escritas = resenaRepository.findByUsuarioAutor_IdUsuarioAndEstadoTrueOrderByIdResenaDesc(idUsuario)
                .stream().map(this::convertir).toList();
        return new ResenasPerfilDto(recibidas, escritas);
    }

    @Override
    @Transactional
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

        return convertir(resenaRepository.save(resena));
    }

    private ResenaResponseDto convertir(ResenaEntity resena) {
        UsuarioEntity autor = resena.getUsuarioAutor();
        PropiedadEntity propiedad = resena.getPropiedad();
        UsuarioEntity objetivo = resena.getUsuarioObjetivo();

        ResenaResponseDto response = new ResenaResponseDto();
        response.setIdResena(resena.getIdResena());
        response.setIdUsuarioAutor(autor.getIdUsuario());
        response.setNombreAutor(autor.getNombre());
        response.setApellidoAutor(autor.getApellido());
        if (propiedad != null) {
            response.setIdPropiedad(propiedad.getIdPropiedad());
            response.setTituloPropiedad(propiedad.getTitulo());
        }
        if (objetivo != null) {
            response.setIdUsuarioObjetivo(objetivo.getIdUsuario());
            response.setNombreObjetivo(objetivo.getNombre() + " " + objetivo.getApellido());
        }
        response.setCalificacion(resena.getCalificacion());
        response.setComentario(resena.getComentario());
        response.setFechaCreacion(resena.getFechaCreacion());
        response.setEstado(resena.getEstado());
        return response;
    }
}
