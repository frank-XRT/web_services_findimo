package com.findimo.api.Controller;

import java.util.UUID;

import com.findimo.api.Service.FotosBaseDatos;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("cloud")
public class FotosController {
    private final FotosBaseDatos fotos;

    public FotosController(FotosBaseDatos fotos) {
        this.fotos = fotos;
    }

    @GetMapping(FotosBaseDatos.RUTA + "{id}")
    public ResponseEntity<byte[]> obtener(@PathVariable UUID id) {
        return fotos.obtener(id)
                .map(foto -> ResponseEntity.ok().contentType(MediaType.parseMediaType(foto.tipo()))
                        .body(foto.contenido()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
