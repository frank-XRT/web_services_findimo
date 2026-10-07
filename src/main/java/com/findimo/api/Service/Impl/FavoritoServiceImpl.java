package com.findimo.api.Service.Impl;

import java.util.ArrayList;
import java.util.List;

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
import com.findimo.api.Service.FavoritoService;

@Service
public class FavoritoServiceImpl implements FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private PerfilEstudianteRepository perfilEstudianteRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Override
    public FavoritoResponseDto registrarFavorito(FavoritoRequestDto dto) {

        PerfilEstudianteEntity perfilEstudiante =
                perfilEstudianteRepository.getReferenceById(dto.getIdPerfilEstudiante());

        PropiedadEntity propiedad =
                propiedadRepository.getReferenceById(dto.getIdPropiedad());

        FavoritoEntity favorito =
                favoritoRepository.findByPerfilEstudianteAndPropiedad(
                        perfilEstudiante,
                        propiedad
                );

        if (favorito != null) {
            favorito.setEstado(true);
        } else {
            favorito = new FavoritoEntity();
            favorito.setPerfilEstudiante(perfilEstudiante);
            favorito.setPropiedad(propiedad);
            favorito.setEstado(true);
        }

        FavoritoEntity favoritoGuardado =
                favoritoRepository.save(favorito);

        FavoritoResponseDto response =
                new FavoritoResponseDto();

        response.setIdFavorito(favoritoGuardado.getIdFavorito());
        response.setIdPerfilEstudiante(perfilEstudiante.getIdPerfilEstudiante());
        response.setIdPropiedad(propiedad.getIdPropiedad());
        response.setEstado(favoritoGuardado.getEstado());

        return response;
    }

    @Override
    public FavoritoResponseDto cambiarEstadoFavorito(Long idFavorito, Boolean estado) {

        FavoritoEntity favorito =
                favoritoRepository.getReferenceById(idFavorito);

        favorito.setEstado(estado);

        FavoritoEntity favoritoGuardado =
                favoritoRepository.save(favorito);

        FavoritoResponseDto response =
                new FavoritoResponseDto();

        response.setIdFavorito(favoritoGuardado.getIdFavorito());
        response.setIdPerfilEstudiante(
                favoritoGuardado.getPerfilEstudiante().getIdPerfilEstudiante()
        );
        response.setIdPropiedad(
                favoritoGuardado.getPropiedad().getIdPropiedad()
        );
        response.setEstado(favoritoGuardado.getEstado());

        return response;
    }

    @Override
    public List<FavoritoResponseDto> listarFavoritosPorPerfilEstudiante(Long idPerfilEstudiante) {

        PerfilEstudianteEntity perfilEstudiante =
                perfilEstudianteRepository.getReferenceById(idPerfilEstudiante);

        List<FavoritoEntity> favoritos =
                favoritoRepository.findByPerfilEstudianteAndEstadoTrue(
                        perfilEstudiante
                );

        List<FavoritoResponseDto> response =
                new ArrayList<>();

        for (FavoritoEntity favorito : favoritos) {

            FavoritoResponseDto dto =
                    new FavoritoResponseDto();

            dto.setIdFavorito(favorito.getIdFavorito());
            dto.setIdPerfilEstudiante(
                    favorito.getPerfilEstudiante().getIdPerfilEstudiante()
            );
            dto.setIdPropiedad(
                    favorito.getPropiedad().getIdPropiedad()
            );
            dto.setEstado(favorito.getEstado());

            response.add(dto);
        }

        return response;
    }
}