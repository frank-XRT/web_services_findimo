package com.findimo.api.Service.Impl;

import com.findimo.api.Dto.AuditoriaRequestDto;
import com.findimo.api.Dto.AuditoriaResponseDto;
import com.findimo.api.Entity.AuditoriaEntity;
import com.findimo.api.Repository.AuditoriaRepository;
import com.findimo.api.Service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuditoriaServiceImpl implements AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @Override
    public AuditoriaResponseDto registrarAuditoria(AuditoriaRequestDto dto) {

        AuditoriaEntity auditoria = new AuditoriaEntity();

        auditoria.setIdUsuarioEditar(dto.getIdUsuarioEditar());
        auditoria.setIdUsuarioEliminar(dto.getIdUsuarioEliminar());
        auditoria.setDatosAnteriores(dto.getDatosAnteriores());
        auditoria.setDatosNuevos(dto.getDatosNuevos());
        auditoria.setEstado(dto.getEstado() == null || dto.getEstado());

        AuditoriaEntity guardada = auditoriaRepository.save(auditoria);

        AuditoriaResponseDto response = new AuditoriaResponseDto();

        response.setIdAuditoria(guardada.getIdAuditoria());
        response.setIdUsuarioEditar(guardada.getIdUsuarioEditar());
        response.setIdUsuarioEliminar(guardada.getIdUsuarioEliminar());
        response.setDatosAnteriores(guardada.getDatosAnteriores());
        response.setDatosNuevos(guardada.getDatosNuevos());
        response.setFechaRegistro(guardada.getFechaRegistro());
        response.setEstado(guardada.getEstado());

        return response;
    }

    @Override
    public List<AuditoriaResponseDto> listarAuditorias() {

        List<AuditoriaEntity> lista = auditoriaRepository.findAll();
        List<AuditoriaResponseDto> respuestas = new ArrayList<>();

        for (AuditoriaEntity auditoria : lista) {

            AuditoriaResponseDto response = new AuditoriaResponseDto();

            response.setIdAuditoria(auditoria.getIdAuditoria());
            response.setIdUsuarioEditar(auditoria.getIdUsuarioEditar());
            response.setIdUsuarioEliminar(auditoria.getIdUsuarioEliminar());
            response.setDatosAnteriores(auditoria.getDatosAnteriores());
            response.setDatosNuevos(auditoria.getDatosNuevos());
            response.setFechaRegistro(auditoria.getFechaRegistro());
            response.setEstado(auditoria.getEstado());

            respuestas.add(response);
        }

        return respuestas;
    }
}