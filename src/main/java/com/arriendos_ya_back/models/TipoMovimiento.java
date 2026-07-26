package com.arriendos_ya_back.models;

public enum TipoMovimiento {
    INGRESO("ingreso"),
    EGRESO("egreso");

    private final String valor;

    TipoMovimiento(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static TipoMovimiento fromValor(String valor) {
        for (TipoMovimiento tipo : TipoMovimiento.values()) {
            if (tipo.valor.equalsIgnoreCase(valor)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de movimiento inválido: " + valor);
    }
}
