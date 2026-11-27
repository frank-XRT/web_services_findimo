package com.findimo.api.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.findimo.api.Dto.SolicitudAlquilerRequestDto;
import com.findimo.api.Dto.SolicitudAlquilerResponseDto;
import com.findimo.api.Service.SolicitudAlquilerService;
import com.findimo.api.Repository.PerfilEstudianteRepository;

@RestController
@RequestMapping("/api/solicitudes-alquiler")
public class SolicitudAlquilerController {

    @Autowired
    private SolicitudAlquilerService solicitudAlquilerService;

    @Autowired
    private PerfilEstudianteRepository perfiles;

    @PostMapping
    public SolicitudAlquilerResponseDto registrarSolicitud(@RequestBody SolicitudAlquilerRequestDto dto,
            HttpServletRequest request) {
        var sesion = request.getSession(false);
        if (sesion == null || !(sesion.getAttribute(UsuarioController.USUARIO_EN_SESION) instanceof Long cuenta)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (dto.getIdPerfilEstudiante() == null || dto.getIdPropiedad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (!perfiles.existsByIdPerfilEstudianteAndUsuario_IdUsuario(dto.getIdPerfilEstudiante(), cuenta)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return solicitudAlquilerService.registrarSolicitud(dto);
    }
}
