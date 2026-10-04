package com.findimo.api.Controller;

import com.findimo.api.Dto.ResenaResponseDto;
import com.findimo.api.Service.ResenaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
public class ResenaController {

    private final ResenaService resenaService;

    @GetMapping("/referencia/{id}")
    public ResponseEntity<List<ResenaResponseDto>> listarResenasPorPropiedad(
            @PathVariable Long idPropiedad) {

        List<ResenaResponseDto> resenas =
                resenaService.listarResenasPorPropiedad(idPropiedad);

        return ResponseEntity.ok(resenas);
    }
}