package com.findimo.api.Service.Impl;

import com.findimo.api.Dto.RegistroUsuarioRequestDto;
import com.findimo.api.Dto.RegistroUsuarioResponseDto;
import com.findimo.api.Entity.PerfilArrendadorEntity;
import com.findimo.api.Entity.PerfilEstudianteEntity;
import com.findimo.api.Entity.RolEntity;
import com.findimo.api.Entity.UsuarioEntity;
import com.findimo.api.Repository.PerfilArrendadorRepository;
import com.findimo.api.Repository.PerfilEstudianteRepository;
import com.findimo.api.Repository.RolRepository;
import com.findimo.api.Repository.UsuarioRepository;
import com.findimo.api.Service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PerfilEstudianteRepository perfilEstudianteRepository;

    @Autowired
    private PerfilArrendadorRepository perfilArrendadorRepository;

    @Override
    @Transactional
    public RegistroUsuarioResponseDto registrarUsuario(RegistroUsuarioRequestDto dto) {

        Optional<RolEntity> rolOpcional = rolRepository.findByNombreRol(dto.getNombreRol());

        if (rolOpcional.isEmpty()) {
            throw new RuntimeException("Error: El rol '" + dto.getNombreRol() + "' no existe en la BD.");
        }

        RolEntity rol = rolOpcional.get();

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setRol(rol);
        usuario.setCorreoElectronico(dto.getCorreoElectronico());
        usuario.setContrasena(dto.getContrasena());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setTelefono(dto.getTelefono());

        UsuarioEntity usuarioGuardado = usuarioRepository.save(usuario);

        RegistroUsuarioResponseDto response = new RegistroUsuarioResponseDto();
        response.setIdUsuario(usuarioGuardado.getIdUsuario());
        response.setNombre(usuarioGuardado.getNombre());
        response.setApellido(usuarioGuardado.getApellido());
        response.setCorreoElectronico(usuarioGuardado.getCorreoElectronico());
        response.setTelefono(usuarioGuardado.getTelefono());
        response.setNombreRol(rol.getNombreRol());

        if (dto.getNombreRol().equalsIgnoreCase("ESTUDIANTE")) {

            PerfilEstudianteEntity perfilEstudiante = new PerfilEstudianteEntity();
            perfilEstudiante.setUsuario(usuarioGuardado);
            perfilEstudiante.setNombreUniversidad(dto.getNombreUniversidad());
            perfilEstudiante.setPresupuestoMaximo(dto.getPresupuestoMaximo());

            PerfilEstudianteEntity perfilGuardado = perfilEstudianteRepository.save(perfilEstudiante);

            response.setIdPerfilEstudiante(perfilGuardado.getIdPerfilEstudiante());
            response.setNombreUniversidad(perfilGuardado.getNombreUniversidad());
            response.setPresupuestoMaximo(perfilGuardado.getPresupuestoMaximo());
            response.setMensaje("Estudiante registrado con éxito");

        } else if (dto.getNombreRol().equalsIgnoreCase("ARRENDADOR")) {

            PerfilArrendadorEntity perfilArrendador = new PerfilArrendadorEntity();
            perfilArrendador.setUsuario(usuarioGuardado);
            perfilArrendador.setCuentaBancaria(dto.getCuentaBancaria());

            PerfilArrendadorEntity perfilGuardado = perfilArrendadorRepository.save(perfilArrendador);

            response.setIdPerfilArrendador(perfilGuardado.getIdPerfilArrendador());
            response.setCuentaBancaria(perfilGuardado.getCuentaBancaria());
            response.setMensaje("Arrendador registrado con éxito");

        } else {
            throw new RuntimeException("El rol ingresado no es válido.");
        }

        return response;
    }
}