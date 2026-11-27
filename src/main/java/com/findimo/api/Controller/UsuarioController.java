package com.findimo.api.Controller;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.findimo.api.Dto.LoginRequestDto;
import com.findimo.api.Dto.ActivarPerfilDto;
import com.findimo.api.Dto.RegistroUsuarioRequestDto;
import com.findimo.api.Dto.RegistroUsuarioResponseDto;
import com.findimo.api.Service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    static final String USUARIO_EN_SESION = "idUsuario";

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public RegistroUsuarioResponseDto registrarUsuario(@RequestBody RegistroUsuarioRequestDto dto, HttpServletRequest request) {
        RegistroUsuarioResponseDto usuario = usuarioService.registrarUsuario(dto);
        iniciarSesionHttp(request, usuario);
        return usuario;
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody LoginRequestDto dto, HttpServletRequest request) {
        try {
            RegistroUsuarioResponseDto response = usuarioService.iniciarSesion(dto);
            iniciarSesionHttp(request, response);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", ex.getMessage()));
        }
    }

    public record ContrasenaPropia(String contrasena) {}

    @PatchMapping("/contrasena")
    public ResponseEntity<Void> actualizarContrasena(@RequestBody ContrasenaPropia datos, HttpServletRequest request) {
        var sesion = request.getSession(false);
        if (sesion == null || !(sesion.getAttribute(USUARIO_EN_SESION) instanceof Long cuenta)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        usuarioService.actualizarContrasenaPropia(cuenta, datos.contrasena());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/perfiles")
    public RegistroUsuarioResponseDto activarPerfil(@RequestBody ActivarPerfilDto dto, HttpServletRequest request) {
        var sesion = request.getSession(false);
        if (sesion == null || !(sesion.getAttribute(USUARIO_EN_SESION) instanceof Long cuenta)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return usuarioService.activarPerfil(cuenta, dto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> cerrarSesion(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        if (sesion != null) {
            sesion.invalidate();
        }
        return ResponseEntity.noContent().build();
    }

    private void iniciarSesionHttp(HttpServletRequest request, RegistroUsuarioResponseDto usuario) {
        HttpSession sesion = request.getSession(true);
        request.changeSessionId();
        sesion.setAttribute(USUARIO_EN_SESION, usuario.getIdUsuario());
    }
}
