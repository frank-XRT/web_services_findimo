package com.findimo.api.Controller;

import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.findimo.api.Repository.PropiedadRepository;
import com.findimo.api.Repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios/favoritos")
public class FavoritosCuentaController {

    private final UsuarioRepository usuarios;
    private final PropiedadRepository propiedades;

    public FavoritosCuentaController(UsuarioRepository usuarios, PropiedadRepository propiedades) {
        this.usuarios = usuarios;
        this.propiedades = propiedades;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<Set<Long>> listar(@RequestParam("cuenta") Long cuenta, HttpServletRequest request) {
        Long id = verificarCuenta(cuenta, request);
        var usuario = usuarios.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return ResponseEntity.ok().cacheControl(CacheControl.noStore())
                .body(Set.copyOf(usuario.getPropiedadesFavoritas()));
    }

    @PutMapping("/{idPropiedad}")
    @Transactional
    public ResponseEntity<Void> guardar(@PathVariable("idPropiedad") Long idPropiedad,
            @RequestParam("cuenta") Long cuenta, @RequestParam("activo") boolean activo, HttpServletRequest request) {
        Long id = verificarCuenta(cuenta, request);
        var usuario = usuarios.findByIdUsuario(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        if (activo) {
            if (!propiedades.existsById(idPropiedad)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            usuario.getPropiedadesFavoritas().add(idPropiedad);
        } else {
            usuario.getPropiedadesFavoritas().remove(idPropiedad);
        }
        return ResponseEntity.noContent().build();
    }

    private Long verificarCuenta(Long cuenta, HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        if (sesion == null || !cuenta.equals(sesion.getAttribute(UsuarioController.USUARIO_EN_SESION))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return cuenta;
    }
}
