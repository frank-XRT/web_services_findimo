package com.findimo.api.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.findimo.api.Dto.ResenaRequestDto;
import com.findimo.api.Dto.ResenaResponseDto;
import com.findimo.api.Service.ResenaService;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @GetMapping("/propiedad/{idPropiedad}")
    public List<ResenaResponseDto> listarResenasPorPropiedad(@PathVariable Long idPropiedad) {

        List<ResenaResponseDto> resenas = resenaService.listarResenasPorPropiedad(idPropiedad);

        return resenas;
    }

    @PostMapping
    public ResenaResponseDto registrarResena(@RequestBody ResenaRequestDto dto) {

        ResenaResponseDto response = resenaService.registrarResena(dto);

        return response;
    }
}