package com.findimo.api.Dto;

import java.util.List;

public record ResenasPerfilDto(List<ResenaResponseDto> recibidas, List<ResenaResponseDto> escritas) {}
