package com.findimo.api.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.findimo.api.Dto.FavoritoRequestDto;
import com.findimo.api.Dto.FavoritoResponseDto;
import com.findimo.api.Service.FavoritoService;

@RestController
@RequestMapping("/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @PostMapping
    public FavoritoResponseDto registrarFavorito(@RequestBody FavoritoRequestDto dto) {

        FavoritoResponseDto response = favoritoService.registrarFavorito(dto);

        return response;
    }

    @PutMapping("/{idFavorito}/estado")
    public FavoritoResponseDto cambiarEstadoFavorito(
            @PathVariable Long idFavorito,
            @RequestParam Boolean estado) {

        FavoritoResponseDto response =
                favoritoService.cambiarEstadoFavorito(idFavorito, estado);

        return response;
    }

    @GetMapping("/perfil/{idPerfilEstudiante}")
    public List<FavoritoResponseDto> listarFavoritos(
            @PathVariable Long idPerfilEstudiante) {

        List<FavoritoResponseDto> favoritos =
                favoritoService.listarFavoritosPorPerfilEstudiante(idPerfilEstudiante);

        return favoritos;
    }
}