package com.findimo.api.Service.Impl;

import java.util.ArrayList;
import java.util.List;
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

        // Buscar perfil estudiante
        Optional<PerfilEstudianteEntity> perfilOptional =
                perfilEstudianteRepository.findById(dto.getIdPerfilEstudiante());

        if (perfilOptional.isEmpty()) {
            throw new RuntimeException(
                    "El perfil estudiante no existe."
            );
        }

        PerfilEstudianteEntity perfilEstudiante =
                perfilOptional.get();

        // Buscar propiedad
        Optional<PropiedadEntity> propiedadOptional =
                propiedadRepository.findById(dto.getIdPropiedad());

        if (propiedadOptional.isEmpty()) {
            throw new RuntimeException(
                    "La propiedad no existe."
            );
        }

        PropiedadEntity propiedad =
                propiedadOptional.get();

        // Verificar si ya existe el favorito
        Optional<FavoritoEntity> favoritoOptional =
                favoritoRepository
                        .findByPerfilEstudiante_IdPerfilEstudianteAndPropiedad_IdPropiedad(
                                dto.getIdPerfilEstudiante(),
                                dto.getIdPropiedad()
                        );

        FavoritoEntity favorito;

        if (favoritoOptional.isPresent()) {

            // Si ya existe, solamente se vuelve a activar
            favorito = favoritoOptional.get();
            favorito.setEstado(true);

        } else {

            // Crear nuevo favorito
            favorito = new FavoritoEntity();

            favorito.setPerfilEstudiante(perfilEstudiante);
            favorito.setPropiedad(propiedad);
            favorito.setEstado(true);
        }

        FavoritoEntity favoritoGuardado =
                favoritoRepository.save(favorito);

        // Crear respuesta
        FavoritoResponseDto response =
                new FavoritoResponseDto();

        response.setIdFavorito(
                favoritoGuardado.getIdFavorito()
        );

        response.setIdPerfilEstudiante(
                perfilEstudiante.getIdPerfilEstudiante()
        );

        response.setIdPropiedad(
                propiedad.getIdPropiedad()
        );

        response.setEstado(
                favoritoGuardado.getEstado()
        );

        return response;
    }

    @Override
    public FavoritoResponseDto cambiarEstadoFavorito(
            Long idFavorito,
            Boolean estado) {

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

        // Cambiar estado
        favorito.setEstado(estado);

        FavoritoEntity favoritoGuardado =
                favoritoRepository.save(favorito);

        // Crear respuesta
        FavoritoResponseDto response =
                new FavoritoResponseDto();

        response.setIdFavorito(
                favoritoGuardado.getIdFavorito()
        );

        response.setIdPerfilEstudiante(
                favoritoGuardado
                        .getPerfilEstudiante()
                        .getIdPerfilEstudiante()
        );

        response.setIdPropiedad(
                favoritoGuardado
                        .getPropiedad()
                        .getIdPropiedad()
        );

        response.setEstado(
                favoritoGuardado.getEstado()
        );

        return response;
    }

    @Override
    public List<FavoritoResponseDto> listarFavoritosPorPerfilEstudiante(
            Long idPerfilEstudiante) {

        List<FavoritoEntity> favoritos =
                favoritoRepository
                        .findByPerfilEstudiante_IdPerfilEstudianteAndEstadoTrue(
                                idPerfilEstudiante
                        );

        List<FavoritoResponseDto> response =
                new ArrayList<>();

        for (FavoritoEntity favorito : favoritos) {

            FavoritoResponseDto dto =
                    new FavoritoResponseDto();

            dto.setIdFavorito(
                    favorito.getIdFavorito()
            );

            dto.setIdPerfilEstudiante(
                    favorito.getPerfilEstudiante()
                            .getIdPerfilEstudiante()
            );

            dto.setIdPropiedad(
                    favorito.getPropiedad()
                            .getIdPropiedad()
            );

            dto.setEstado(
                    favorito.getEstado()
            );

            response.add(dto);
        }

        return response;
    }
}