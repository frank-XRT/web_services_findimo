package com.findimo.api.Dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	private Boolean estado;
}
