package com.findimo.api.Controller;

import com.findimo.api.Dto.RegistroUsuarioRequestDto;
import com.findimo.api.Dto.RegistroUsuarioResponseDto;
import com.findimo.api.Service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public RegistroUsuarioResponseDto registrarUsuario(@RequestBody RegistroUsuarioRequestDto dto) {

        RegistroUsuarioResponseDto response = usuarioService.registrarUsuario(dto);

        return response;
    }
}