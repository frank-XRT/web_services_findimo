package com.findimo.api.Controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.findimo.api.Dto.ResenaRequestDto;
import com.findimo.api.Dto.ResenaResponseDto;
import com.findimo.api.Dto.ResenasPerfilDto;
import com.findimo.api.Service.ResenaService;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @GetMapping("/perfil")
    public ResponseEntity<ResenasPerfilDto> listarResenasDePerfil(@RequestParam Long cuenta, HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        if (sesion == null || !(sesion.getAttribute(UsuarioController.USUARIO_EN_SESION) instanceof Long idUsuario) || !idUsuario.equals(cuenta)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(resenaService.listarResenasDePerfil(idUsuario));
    }

    @GetMapping("/propiedad/{idPropiedad}")
    public List<ResenaResponseDto> listarResenasPorPropiedad(@PathVariable Long idPropiedad) {

        List<ResenaResponseDto> resenas = resenaService.listarResenasPorPropiedad(idPropiedad);

        return resenas;
    }

    @GetMapping("/propiedad/{idPropiedad}/arrendador")
    public List<ResenaResponseDto> listarResenasDelArrendador(@PathVariable Long idPropiedad) {
        return resenaService.listarResenasDelArrendador(idPropiedad);
    }

    @PostMapping
    public ResenaResponseDto registrarResena(@RequestBody ResenaRequestDto dto) {

        ResenaResponseDto response = resenaService.registrarResena(dto);

        return response;
    }
}