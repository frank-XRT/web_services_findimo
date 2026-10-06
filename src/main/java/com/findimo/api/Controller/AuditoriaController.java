package com.findimo.api.Controller;

import com.findimo.api.Dto.AuditoriaRequestDto;
import com.findimo.api.Dto.AuditoriaResponseDto;
import com.findimo.api.Service.AuditoriaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @PostMapping
    public ResponseEntity<AuditoriaResponseDto> registrarAuditoria(
            @RequestBody AuditoriaRequestDto dto) {

        AuditoriaResponseDto response =
                auditoriaService.registrarAuditoria(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<AuditoriaResponseDto>> listarAuditorias() {

        List<AuditoriaResponseDto> auditorias =
                auditoriaService.listarAuditorias();

        return ResponseEntity.ok(auditorias);
    }

    @GetMapping("/{idAuditoria}")
    public ResponseEntity<AuditoriaResponseDto> obtenerAuditoria(
            @PathVariable Long idAuditoria) {

        AuditoriaResponseDto response =
                auditoriaService.obtenerAuditoria(idAuditoria);

        return ResponseEntity.ok(response);
    }
}