package com.findimo.api.Service;

import com.findimo.api.Dto.RegistroUsuarioRequestDto;
import com.findimo.api.Dto.RegistroUsuarioResponseDto;

public interface UsuarioService {
    RegistroUsuarioResponseDto registrarUsuario(RegistroUsuarioRequestDto dto);
}