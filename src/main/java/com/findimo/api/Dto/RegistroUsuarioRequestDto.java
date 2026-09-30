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
public class RegistroUsuarioRequestDto {
    private String correoElectronico;
    private String contrasena;
    private String nombre;
    private String apellido;
    private String telefono;
    private String nombreRol;
    private String nombreUniversidad;
    private BigDecimal presupuestoMaximo;

    private String cuentaBancaria;
}