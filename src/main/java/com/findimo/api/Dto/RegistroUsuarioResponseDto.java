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
public class RegistroUsuarioResponseDto {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String correoElectronico;
    private String telefono;
    private String nombreRol;

    private Long idPerfilEstudiante;
    private Long idPerfilArrendador;

    private String nombreUniversidad;
    private BigDecimal presupuestoMaximo;
    private String cuentaBancaria;

    private String mensaje;
}