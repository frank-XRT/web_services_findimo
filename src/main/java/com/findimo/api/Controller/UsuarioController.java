package com.findimo.api.Controller;

import com.findimo.api.Dto.RegistroUsuarioRequestDto;
import com.findimo.api.Dto.RegistroUsuarioResponseDto;
import com.findimo.api.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<RegistroUsuarioResponseDto> registrarUsuario(@RequestBody RegistroUsuarioRequestDto dto) {
        RegistroUsuarioResponseDto response = usuarioService.registrarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
