package com.arriendos_ya_back.models;

public enum DiaPago {
    DIA_5(5),
    DIA_10(10),
    DIA_15(15),
    DIA_20(20),
    DIA_25(25),
    DIA_30(30);

    private final Integer valor;

    DiaPago(Integer valor) {
        this.valor = valor;
    }

    public Integer getValor() {
        return valor;
    }

    public static DiaPago fromValor(Integer valor) {
        for (DiaPago dia : DiaPago.values()) {
            if (dia.valor.equals(valor)) {
                return dia;
            }
        }
        throw new IllegalArgumentException("Día de pago inválido: " + valor + ". Valores permitidos: 5, 10, 15, 20, 25, 30");
    }
}
