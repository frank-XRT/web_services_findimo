package com.findimo.api.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.findimo.api.Dto.SolicitudAlquilerRequestDto;
import com.findimo.api.Dto.SolicitudAlquilerResponseDto;
import com.findimo.api.Service.SolicitudAlquilerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/solicitudes-alquiler")
@RequiredArgsConstructor
public class SolicitudAlquilerController {

    private final SolicitudAlquilerService solicitudAlquilerService;


    @PostMapping
    public ResponseEntity<SolicitudAlquilerResponseDto>
    registrarSolicitud(
            @RequestBody SolicitudAlquilerRequestDto dto) {

        SolicitudAlquilerResponseDto response =
                solicitudAlquilerService.registrarSolicitud(
                        dto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}