package com.findimo.api.Controller;

import com.findimo.api.Dto.AuditoriaResponseDto;
import com.findimo.api.Service.AuditoriaService;
import com.findimo.api.Service.AdministradorService;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.CacheControl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;
    private final AdministradorService administrador;

    @GetMapping
    public ResponseEntity<List<AuditoriaResponseDto>> listarAuditorias(HttpServletRequest request) {
        administrador.verificarCuenta(AdministradorController.cuentaEnSesion(request));

        List<AuditoriaResponseDto> auditorias =
                auditoriaService.listarAuditorias();

        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(auditorias);
    }

}