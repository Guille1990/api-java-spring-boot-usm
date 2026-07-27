package com.arriendos_ya_back.dto;

import java.util.List;

public class EnviarReporteRequest {

    private List<String> destinatarios;

    public List<String> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(List<String> destinatarios) {
        this.destinatarios = destinatarios;
    }
}
