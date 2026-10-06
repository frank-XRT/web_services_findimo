package com.findimo.api.Service;

import java.util.Map;

public interface AuditoriaService {

    void registrarAuditoria(
            Long idUsuarioEditar,
            Long idUsuarioEliminar,
            Map<String, Object> datosAnteriores,
            Map<String, Object> datosNuevos
    );
}