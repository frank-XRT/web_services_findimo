package com.findimo.api.Service;

import com.findimo.api.Dto.SolicitudAlquilerRequestDto;
import com.findimo.api.Dto.SolicitudAlquilerResponseDto;

public interface SolicitudAlquilerService {

    SolicitudAlquilerResponseDto registrarSolicitud(
            SolicitudAlquilerRequestDto dto
    );
}