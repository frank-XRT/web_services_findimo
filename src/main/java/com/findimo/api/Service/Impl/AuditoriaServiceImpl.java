package com.findimo.api.Service.Impl;

import com.findimo.api.Dto.AuditoriaRequestDto;
import com.findimo.api.Dto.AuditoriaResponseDto;
import com.findimo.api.Entity.AuditoriaEntity;
import com.findimo.api.Repository.AuditoriaRepository;
import com.findimo.api.Service.AuditoriaService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    @Override
    @Transactional
    public AuditoriaResponseDto registrarAuditoria(AuditoriaRequestDto dto) {

        AuditoriaEntity auditoria = AuditoriaEntity.builder()
                .idUsuarioEditar(dto.getIdUsuarioEditar())
                .idUsuarioEliminar(dto.getIdUsuarioEliminar())
                .datosAnteriores(dto.getDatosAnteriores())
                .datosNuevos(dto.getDatosNuevos())
                .estado(dto.getEstado() != null ? dto.getEstado() : true)
                .build();

        AuditoriaEntity auditoriaGuardada =
                auditoriaRepository.save(auditoria);

        return convertirResponse(auditoriaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditoriaResponseDto> listarAuditorias() {

        return auditoriaRepository.findAll()
                .stream()
                .map(this::convertirResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AuditoriaResponseDto obtenerAuditoria(Long idAuditoria) {

        AuditoriaEntity auditoria = auditoriaRepository
                .findById(idAuditoria)
                .orElseThrow(() ->
                        new RuntimeException("La auditoria no existe.")
                );

        return convertirResponse(auditoria);
    }

    private AuditoriaResponseDto convertirResponse(
            AuditoriaEntity auditoria) {

        return AuditoriaResponseDto.builder()
                .idAuditoria(auditoria.getIdAuditoria())
                .idUsuarioEditar(auditoria.getIdUsuarioEditar())
                .idUsuarioEliminar(auditoria.getIdUsuarioEliminar())
                .datosAnteriores(auditoria.getDatosAnteriores())
                .datosNuevos(auditoria.getDatosNuevos())
                .fechaRegistro(auditoria.getFechaRegistro())
                .estado(auditoria.getEstado())
                .build();
    }
}