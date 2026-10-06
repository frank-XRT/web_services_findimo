package com.findimo.api.Controller;

import com.findimo.api.Dto.ResenaRequestDto;
import com.findimo.api.Dto.ResenaResponseDto;
import com.findimo.api.Service.ResenaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
public class ResenaController {

    private final ResenaService resenaService;

    @GetMapping("/propiedad/{idPropiedad}")
    public ResponseEntity<List<ResenaResponseDto>> listarResenasPorPropiedad(
            @PathVariable("idPropiedad") Long idPropiedad) {

        List<ResenaResponseDto> resenas =
                resenaService.listarResenasPorPropiedad(idPropiedad);

        return ResponseEntity.ok(resenas);
    }

    @PostMapping
    public ResponseEntity<ResenaResponseDto> registrarResena(
            @RequestBody ResenaRequestDto dto) {

        ResenaResponseDto response =
                resenaService.registrarResena(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}