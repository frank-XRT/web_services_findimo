package com.findimo.api.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.findimo.api.Dto.FavoritoRequestDto;
import com.findimo.api.Dto.FavoritoResponseDto;
import com.findimo.api.Service.FavoritoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;

    @PostMapping
    public ResponseEntity<FavoritoResponseDto> registrarFavorito(
            @RequestBody FavoritoRequestDto dto) {

        FavoritoResponseDto response =
                favoritoService.registrarFavorito(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{idFavorito}/estado")
    public ResponseEntity<FavoritoResponseDto> cambiarEstadoFavorito(
            @PathVariable Long idFavorito,
            @RequestParam Boolean estado) {

        FavoritoResponseDto response =
                favoritoService.cambiarEstadoFavorito(
                        idFavorito,
                        estado
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/perfil/{idPerfilEstudiante}")
    public ResponseEntity<List<FavoritoResponseDto>> listarFavoritos(
            @PathVariable Long idPerfilEstudiante) {

        List<FavoritoResponseDto> favoritos =
                favoritoService
                        .listarFavoritosPorPerfilEstudiante(
                                idPerfilEstudiante
                        );

        return ResponseEntity.ok(favoritos);
    }
}




