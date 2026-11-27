package com.findimo.api.Controller;

import com.findimo.api.Service.AdministradorService;
import com.findimo.api.Service.AdministradorService.Pagina;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdministradorController {
    private final AdministradorService administrador;

    public AdministradorController(AdministradorService administrador) {
        this.administrador = administrador;
    }

    static Long cuentaEnSesion(HttpServletRequest request) {
        var sesion = request.getSession(false);
        return sesion != null && sesion.getAttribute(UsuarioController.USUARIO_EN_SESION) instanceof Long cuenta
                ? cuenta : null;
    }

    public record CambioEstado(Boolean activo) {}

    @PatchMapping("/{seccion}/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable String seccion, @PathVariable Long id, @RequestBody CambioEstado cambio,
                                               HttpServletRequest request) {
        Long cuenta = cuentaEnSesion(request);
        administrador.verificarCuenta(cuenta);
        if (cambio.activo() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        administrador.cambiarEstado(cuenta, seccion, id, cambio.activo());
        return ResponseEntity.noContent().build();
    }

    public record NuevaContrasena(String contrasena, String confirmacion) {}

    @PatchMapping("/usuarios/{id}/contrasena")
    public ResponseEntity<Void> cambiarContrasena(@PathVariable Long id, @RequestBody NuevaContrasena datos,
                                                  HttpServletRequest request) {
        administrador.cambiarContrasena(cuentaEnSesion(request), id, datos.contrasena(), datos.confirmacion());
        return ResponseEntity.noContent().build();
    }

    public record NuevaCuenta(String correo, String contrasena, String confirmacion) {}

    @PostMapping("/administradores")
    public ResponseEntity<Void> crearSecundario(@RequestBody NuevaCuenta datos, HttpServletRequest request) {
        administrador.crearSecundario(cuentaEnSesion(request), datos.correo(), datos.contrasena(), datos.confirmacion());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{seccion}")
    public ResponseEntity<Pagina> consultar(@PathVariable String seccion,
                                           @RequestParam(defaultValue = "0") int pagina,
                                           @RequestParam(defaultValue = "") String buscar,
                                           HttpServletRequest request) {
        Long cuenta = cuentaEnSesion(request);
        if ("administradores".equals(seccion)) administrador.verificarSupremo(cuenta);
        else administrador.verificarCuenta(cuenta);
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(administrador.consultar(seccion, pagina, buscar));
    }
}
