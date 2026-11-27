package com.findimo.api.Service;

import com.findimo.api.Dto.LoginRequestDto;
import com.findimo.api.Dto.ActivarPerfilDto;
import com.findimo.api.Dto.RegistroUsuarioRequestDto;
import com.findimo.api.Dto.RegistroUsuarioResponseDto;

public interface UsuarioService {
	RegistroUsuarioResponseDto registrarUsuario(RegistroUsuarioRequestDto dto);
	RegistroUsuarioResponseDto iniciarSesion(LoginRequestDto dto);
	void actualizarContrasenaPropia(Long cuenta, String contrasena);
	RegistroUsuarioResponseDto activarPerfil(Long cuenta, ActivarPerfilDto dto);
}
