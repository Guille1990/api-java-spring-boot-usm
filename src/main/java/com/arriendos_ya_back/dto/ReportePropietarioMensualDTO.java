package com.arriendos_ya_back.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

public class ReportePropietarioMensualDTO {

    private String propietarioRut;
    private String propietarioNombreCompleto;
    private Integer anio;
    private int cantidadPropiedades;
    @JsonSerialize(using = PlainDoubleSerializer.class)
    private double totalIngresos;
    @JsonSerialize(using = PlainDoubleSerializer.class)
    private double totalEgresos;
    @JsonSerialize(using = PlainDoubleSerializer.class)
    private double balance;
    private List<ResumenMesDTO> resumenMensual = new ArrayList<>();
    private List<PropiedadResumenDTO> propiedades = new ArrayList<>();

    public String getPropietarioRut() {
        return propietarioRut;
    }

    public void setPropietarioRut(String propietarioRut) {
        this.propietarioRut = propietarioRut;
    }

    public String getPropietarioNombreCompleto() {
        return propietarioNombreCompleto;
    }

    public void setPropietarioNombreCompleto(String propietarioNombreCompleto) {
        this.propietarioNombreCompleto = propietarioNombreCompleto;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public int getCantidadPropiedades() {
        return cantidadPropiedades;
    }

    public void setCantidadPropiedades(int cantidadPropiedades) {
        this.cantidadPropiedades = cantidadPropiedades;
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

    public List<ResumenMesDTO> getResumenMensual() {
        return resumenMensual;
    }

    public void setResumenMensual(List<ResumenMesDTO> resumenMensual) {
        this.resumenMensual = resumenMensual;
    }

    public List<PropiedadResumenDTO> getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(List<PropiedadResumenDTO> propiedades) {
        this.propiedades = propiedades;
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

    public static class PropiedadResumenDTO {
        private Long propiedadId;
        private String direccion;
        private String comuna;
        private String ciudad;
        private String region;
        @JsonSerialize(using = PlainDoubleSerializer.class)
        private double totalIngresos;
        @JsonSerialize(using = PlainDoubleSerializer.class)
        private double totalEgresos;
        @JsonSerialize(using = PlainDoubleSerializer.class)
        private double balance;

        public Long getPropiedadId() {
            return propiedadId;
        }

        public void setPropiedadId(Long propiedadId) {
            this.propiedadId = propiedadId;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public String getComuna() {
            return comuna;
        }

        public void setComuna(String comuna) {
            this.comuna = comuna;
        }

        public String getCiudad() {
            return ciudad;
        }

        public void setCiudad(String ciudad) {
            this.ciudad = ciudad;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
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