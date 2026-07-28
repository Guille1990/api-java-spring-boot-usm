package com.arriendos_ya_back.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

public class ReportePropietariosMensualDTO {

    private Integer anio;
    private String propietarioRutFiltro;
    private int cantidadPropietarios;
    @JsonSerialize(using = PlainDoubleSerializer.class)
    private double totalIngresos;
    @JsonSerialize(using = PlainDoubleSerializer.class)
    private double totalEgresos;
    @JsonSerialize(using = PlainDoubleSerializer.class)
    private double balance;
    private List<ResumenMesDTO> resumenMensualGlobal = new ArrayList<>();
    private List<ReportePropietarioMensualDTO> propietarios = new ArrayList<>();

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getPropietarioRutFiltro() {
        return propietarioRutFiltro;
    }

    public void setPropietarioRutFiltro(String propietarioRutFiltro) {
        this.propietarioRutFiltro = propietarioRutFiltro;
    }

    public int getCantidadPropietarios() {
        return cantidadPropietarios;
    }

    public void setCantidadPropietarios(int cantidadPropietarios) {
        this.cantidadPropietarios = cantidadPropietarios;
    }

    public double getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(double totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public double getTotalEgresos() {
        return totalEgresos;
    }

    public void setTotalEgresos(double totalEgresos) {
        this.totalEgresos = totalEgresos;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<ResumenMesDTO> getResumenMensualGlobal() {
        return resumenMensualGlobal;
    }

    public void setResumenMensualGlobal(List<ResumenMesDTO> resumenMensualGlobal) {
        this.resumenMensualGlobal = resumenMensualGlobal;
    }

    public List<ReportePropietarioMensualDTO> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<ReportePropietarioMensualDTO> propietarios) {
        this.propietarios = propietarios;
    }

    public static class ResumenMesDTO {
        private int mes;
        @JsonSerialize(using = PlainDoubleSerializer.class)
        private double totalIngresos;
        @JsonSerialize(using = PlainDoubleSerializer.class)
        private double totalEgresos;
        @JsonSerialize(using = PlainDoubleSerializer.class)
        private double balance;

        public int getMes() {
            return mes;
        }

        public void setMes(int mes) {
            this.mes = mes;
        }

        public double getTotalIngresos() {
            return totalIngresos;
        }

        public void setTotalIngresos(double totalIngresos) {
            this.totalIngresos = totalIngresos;
        }

        public double getTotalEgresos() {
            return totalEgresos;
        }

        public void setTotalEgresos(double totalEgresos) {
            this.totalEgresos = totalEgresos;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }
    }
}