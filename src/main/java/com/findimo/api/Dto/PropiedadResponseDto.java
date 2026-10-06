package com.findimo.api.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropiedadResponseDto {

    private Long idPropiedad;
    private String titulo;
    private String descripcion;
    private BigDecimal precio;
    private Integer habitaciones;
    private String direccion;
    private String distrito;
    private String estado;
}
