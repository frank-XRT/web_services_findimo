package com.findimo.api.Service.Impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.findimo.api.Entity.AuditoriaEntity;
import com.findimo.api.Repository.AuditoriaRepository;
import com.findimo.api.Service.AuditoriaService;

@Service
public class AuditoriaServiceImpl implements AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void registrarAuditoria(
            Long idUsuarioEditar,
            Long idUsuarioEliminar,
            Map<String, Object> datosAnteriores,
            Map<String, Object> datosNuevos) {

        try {

            String jsonAnterior =
                    datosAnteriores != null
                            ? objectMapper.writeValueAsString(datosAnteriores)
                            : null;

            String jsonNuevo =
                    datosNuevos != null
                            ? objectMapper.writeValueAsString(datosNuevos)
                            : null;

            AuditoriaEntity auditoria =
                    new AuditoriaEntity();

            auditoria.setIdUsuarioEditar(idUsuarioEditar);
            auditoria.setIdUsuarioEliminar(idUsuarioEliminar);
            auditoria.setDatosAnteriores(jsonAnterior);
            auditoria.setDatosNuevos(jsonNuevo);

            // El registro de auditoría queda activo
            auditoria.setEstado(true);

            auditoriaRepository.save(auditoria);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "Error al registrar la auditoría.",
                    e
            );
        }
    }
}