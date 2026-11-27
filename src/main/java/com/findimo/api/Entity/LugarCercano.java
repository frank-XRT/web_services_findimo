package com.findimo.api.Entity;

public record LugarCercano(Tipo tipo, String nombre, Integer minutos, String categoria) {
    public enum Tipo {
        UNIVERSIDAD, MERCADO, BODEGA, PARQUE, SEGURIDAD, TRANSPORTE, PERSONALIZADO
    }
}
