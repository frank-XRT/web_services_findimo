package com.findimo.api.Dto;

import java.math.BigDecimal;

public record ActivarPerfilDto(Tipo tipo, String nombreUniversidad, BigDecimal presupuestoMaximo, String cuentaBancaria) {
    public enum Tipo { ESTUDIANTE, ARRENDADOR }
}
