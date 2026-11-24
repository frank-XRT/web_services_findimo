package com.findimo.api.Service;

import com.findimo.api.Dto.AuditoriaRequestDto;
import com.findimo.api.Dto.AuditoriaResponseDto;

import java.util.List;

public interface AuditoriaService {

    AuditoriaResponseDto registrarAuditoria(AuditoriaRequestDto dto);

    List<AuditoriaResponseDto> listarAuditorias();
}