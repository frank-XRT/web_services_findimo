package com.findimo.api.Service.Impl;

import com.findimo.api.Dto.AuditoriaRequestDto;
import com.findimo.api.Dto.ActivarPerfilDto;
import com.findimo.api.Dto.LoginRequestDto;
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
import com.findimo.api.Service.AuditoriaService;
import com.findimo.api.Service.AdministradorService;
import com.findimo.api.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

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
    @Autowired
    private AuditoriaService auditoriaService;
    @Autowired
    private AdministradorService administrador;
    @Autowired
    private PasswordEncoder claves;

    @Override
    @Transactional
    public RegistroUsuarioResponseDto registrarUsuario(RegistroUsuarioRequestDto dto) {
        if (!"ESTUDIANTE".equals(dto.getNombreRol()) && !"ARRENDADOR".equals(dto.getNombreRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol de registro inválido.");
        }
        if (dto.getCorreoElectronico() != null
                && AdministradorService.CORREO_SUPREMO.equalsIgnoreCase(dto.getCorreoElectronico().strip())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este correo está reservado para administración.");
        }
        administrador.reservarCorreoDisponible(dto.getCorreoElectronico());
        RolEntity rol = rolRepository.findByNombreRol(dto.getNombreRol());

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setRol(rol);
        usuario.setCorreoElectronico(dto.getCorreoElectronico());
        String contrasena = dto.getContrasena();
        if (!admiteBCrypt(contrasena)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingresa una contraseña de hasta 72 bytes.");
        }
        usuario.setContrasena(claves.encode(contrasena));
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
        response.setNombreRol(rol != null ? rol.getNombreRol() : dto.getNombreRol());

        Map<String, Object> datosNuevos = new LinkedHashMap<>();
        datosNuevos.put("idUsuario", usuarioGuardado.getIdUsuario());
        datosNuevos.put("nombre", usuarioGuardado.getNombre());
        datosNuevos.put("apellido", usuarioGuardado.getApellido());
        datosNuevos.put("correoElectronico", usuarioGuardado.getCorreoElectronico());
        datosNuevos.put("telefono", usuarioGuardado.getTelefono());
        datosNuevos.put("nombreRol", rol != null ? rol.getNombreRol() : dto.getNombreRol());

        if (dto.getNombreRol() != null && dto.getNombreRol().equalsIgnoreCase("ESTUDIANTE")) {
            PerfilEstudianteEntity perfilEstudiante = new PerfilEstudianteEntity();
            perfilEstudiante.setUsuario(usuarioGuardado);
            perfilEstudiante.setNombreUniversidad(dto.getNombreUniversidad());
            perfilEstudiante.setPresupuestoMaximo(dto.getPresupuestoMaximo());

            PerfilEstudianteEntity perfilGuardado = perfilEstudianteRepository.save(perfilEstudiante);
            response.setIdPerfilEstudiante(perfilGuardado.getIdPerfilEstudiante());
            response.setNombreUniversidad(perfilGuardado.getNombreUniversidad());
            response.setPresupuestoMaximo(perfilGuardado.getPresupuestoMaximo());
            response.setMensaje("Estudiante registrado con éxito");

            datosNuevos.put("idPerfilEstudiante", perfilGuardado.getIdPerfilEstudiante());
            datosNuevos.put("nombreUniversidad", perfilGuardado.getNombreUniversidad());
            datosNuevos.put("presupuestoMaximo", perfilGuardado.getPresupuestoMaximo());

        } else if (dto.getNombreRol() != null && dto.getNombreRol().equalsIgnoreCase("ARRENDADOR")) {
            PerfilArrendadorEntity perfilArrendador = new PerfilArrendadorEntity();
            perfilArrendador.setUsuario(usuarioGuardado);
            perfilArrendador.setCuentaBancaria(dto.getCuentaBancaria());

            PerfilArrendadorEntity perfilGuardado = perfilArrendadorRepository.save(perfilArrendador);
            response.setIdPerfilArrendador(perfilGuardado.getIdPerfilArrendador());
            response.setCuentaBancaria(perfilGuardado.getCuentaBancaria());
            response.setMensaje("Arrendador registrado con éxito");

            datosNuevos.put("idPerfilArrendador", perfilGuardado.getIdPerfilArrendador());
            datosNuevos.put("cuentaBancaria", perfilGuardado.getCuentaBancaria());
        }

        AuditoriaRequestDto auditoria = new AuditoriaRequestDto();
        auditoria.setIdUsuarioEditar(null);
        auditoria.setIdUsuarioEliminar(null);
        auditoria.setDatosAnteriores(null);
        auditoria.setDatosNuevos(datosNuevos);
        auditoria.setEstado(true);

        auditoriaService.registrarAuditoria(auditoria);

        return response;
    }

    @Override
    @Transactional
    public RegistroUsuarioResponseDto iniciarSesion(LoginRequestDto dto) {
        if (dto.getCorreoElectronico() == null || dto.getContrasena() == null) {
            throw new RuntimeException("Debe ingresar correo electrónico y contraseña.");
        }

        UsuarioEntity usuario = usuarioRepository.findByCorreoElectronico(dto.getCorreoElectronico().trim())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el correo: " + dto.getCorreoElectronico()));

        String guardada = usuario.getContrasena();
        boolean tieneHash = guardada.matches("\\{bcrypt}\\$2[aby]\\$[0-9]{2}\\$[./A-Za-z0-9]{53}");
        boolean coincide = tieneHash
                ? claves.matches(dto.getContrasena(), guardada) : dto.getContrasena().equals(guardada);
        if (!coincide) {
            throw new RuntimeException("Contraseña incorrecta.");
        }

        if (!usuario.isEstado()) {
            throw new RuntimeException("Tu cuenta está desactivada.");
        }

        if (!tieneHash && admiteBCrypt(dto.getContrasena())
                && usuarioRepository.migrarContrasenaAnterior(usuario.getIdUsuario(), guardada, claves.encode(guardada)) == 0) {
            throw new RuntimeException("Vuelve a iniciar sesión.");
        }

        return describirCuenta(usuario);
    }

    private boolean admiteBCrypt(String contrasena) {
        return contrasena != null && !contrasena.isEmpty()
                && contrasena.getBytes(StandardCharsets.UTF_8).length <= 72;
    }

    @Override
    @Transactional
    public void actualizarContrasenaPropia(Long cuenta, String contrasena) {
        if (!admiteBCrypt(contrasena)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (usuarioRepository.actualizarContrasenaActiva(cuenta, claves.encode(contrasena)) == 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    @Transactional
    public RegistroUsuarioResponseDto activarPerfil(Long cuenta, ActivarPerfilDto dto) {
        var usuario = usuarioRepository.findByIdUsuario(cuenta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        if (dto.tipo() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        switch (dto.tipo()) {
            case ESTUDIANTE -> {
                if (perfilEstudianteRepository.findByUsuario_IdUsuario(cuenta).isEmpty()) {
                    if (dto.nombreUniversidad() == null || dto.nombreUniversidad().isBlank()
                            || dto.nombreUniversidad().length() > 255
                            || (dto.presupuestoMaximo() != null && (dto.presupuestoMaximo().signum() <= 0
                            || dto.presupuestoMaximo().scale() > 2
                            || dto.presupuestoMaximo().compareTo(new java.math.BigDecimal("99999999.99")) > 0))) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                    }
                    var perfil = new PerfilEstudianteEntity();
                    perfil.setUsuario(usuario);
                    perfil.setNombreUniversidad(dto.nombreUniversidad().strip());
                    perfil.setPresupuestoMaximo(dto.presupuestoMaximo());
                    perfilEstudianteRepository.save(perfil);
                }
            }
            case ARRENDADOR -> {
                if (perfilArrendadorRepository.findByUsuario_IdUsuario(cuenta).isEmpty()) {
                    if (dto.cuentaBancaria() == null || dto.cuentaBancaria().isBlank() || dto.cuentaBancaria().length() > 100) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                    }
                    var perfil = new PerfilArrendadorEntity();
                    perfil.setUsuario(usuario);
                    perfil.setCuentaBancaria(dto.cuentaBancaria().strip());
                    perfilArrendadorRepository.save(perfil);
                }
            }
        }
        return describirCuenta(usuario);
    }

    private RegistroUsuarioResponseDto describirCuenta(UsuarioEntity usuario) {
        var response = new RegistroUsuarioResponseDto();
        response.setIdUsuario(usuario.getIdUsuario());
        response.setNombre(usuario.getNombre());
        response.setApellido(usuario.getApellido());
        response.setCorreoElectronico(usuario.getCorreoElectronico());
        response.setTelefono(usuario.getTelefono());
        var rol = usuario.getRol();
        response.setNombreRol(rol == null ? null : rol.getNombreRol());

        perfilEstudianteRepository.findByUsuario_IdUsuario(usuario.getIdUsuario()).ifPresent(perfil -> {
            response.setIdPerfilEstudiante(perfil.getIdPerfilEstudiante());
            response.setNombreUniversidad(perfil.getNombreUniversidad());
            response.setPresupuestoMaximo(perfil.getPresupuestoMaximo());
        });
        perfilArrendadorRepository.findByUsuario_IdUsuario(usuario.getIdUsuario()).ifPresent(perfil -> {
            response.setIdPerfilArrendador(perfil.getIdPerfilArrendador());
            response.setCuentaBancaria(perfil.getCuentaBancaria());
        });
        return response;
    }
}
