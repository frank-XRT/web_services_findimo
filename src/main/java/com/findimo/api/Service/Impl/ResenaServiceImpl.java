package com.findimo.api.Service.Impl;

import java.util.ArrayList;
import java.util.List;

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

            response.add(dto);
        }

        return response;
    }
}