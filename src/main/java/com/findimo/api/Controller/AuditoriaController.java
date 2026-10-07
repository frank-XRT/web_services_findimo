package com.findimo.api.Controller;

import com.findimo.api.Dto.AuditoriaResponseDto;
import com.findimo.api.Service.AuditoriaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<List<AuditoriaResponseDto>> listarAuditorias() {

        List<AuditoriaResponseDto> auditorias =
                auditoriaService.listarAuditorias();

        return ResponseEntity.ok(auditorias);
    }

}