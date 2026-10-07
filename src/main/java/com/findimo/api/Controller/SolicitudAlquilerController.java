package com.findimo.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.findimo.api.Dto.SolicitudAlquilerRequestDto;
import com.findimo.api.Dto.SolicitudAlquilerResponseDto;
import com.findimo.api.Service.SolicitudAlquilerService;

@RestController
@RequestMapping("/api/solicitudes-alquiler")
public class SolicitudAlquilerController {

    @Autowired
    private SolicitudAlquilerService solicitudAlquilerService;

    @PostMapping
    public SolicitudAlquilerResponseDto registrarSolicitud(SolicitudAlquilerRequestDto dto) {

        SolicitudAlquilerResponseDto response = solicitudAlquilerService.registrarSolicitud(dto);

        return response;
    }
}