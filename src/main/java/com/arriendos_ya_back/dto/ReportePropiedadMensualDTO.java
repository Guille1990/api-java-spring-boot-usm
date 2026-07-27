package com.arriendos_ya_back.dto;

import com.arriendos_ya_back.models.TipoMovimiento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportePropiedadMensualDTO {

    private Long propiedadId;
    private String direccion;
    private String comuna;
    private String ciudad;
    private String region;
    private Integer mes;
    private Integer anio;

    private double totalIngresos;
    private double totalEgresos;
    private double balance;

    private int diasTotalesMes;
    private int diasOcupados;
    private double porcentajeOcupacion;

    private List<MovimientoResumenDTO> movimientos = new ArrayList<>();
    private List<EventoResumenDTO> eventos = new ArrayList<>();

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

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
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

    public int getDiasTotalesMes() {
        return diasTotalesMes;
    }

    public void setDiasTotalesMes(int diasTotalesMes) {
        this.diasTotalesMes = diasTotalesMes;
    }

    public int getDiasOcupados() {
        return diasOcupados;
    }

    public void setDiasOcupados(int diasOcupados) {
        this.diasOcupados = diasOcupados;
    }

    public double getPorcentajeOcupacion() {
        return porcentajeOcupacion;
    }

    public void setPorcentajeOcupacion(double porcentajeOcupacion) {
        this.porcentajeOcupacion = porcentajeOcupacion;
    }

    public List<MovimientoResumenDTO> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoResumenDTO> movimientos) {
        this.movimientos = movimientos;
    }

    public List<EventoResumenDTO> getEventos() {
        return eventos;
    }

    public void setEventos(List<EventoResumenDTO> eventos) {
        this.eventos = eventos;
    }

    public static class MovimientoResumenDTO {
        private Long id;
        private String concepto;
        private TipoMovimiento tipo;
        private double monto;
        private LocalDate fecha;
        private String urlComprobante;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getConcepto() {
            return concepto;
        }

        public void setConcepto(String concepto) {
            this.concepto = concepto;
        }

        public TipoMovimiento getTipo() {
            return tipo;
        }

        public void setTipo(TipoMovimiento tipo) {
            this.tipo = tipo;
        }

        public double getMonto() {
            return monto;
        }

        public void setMonto(double monto) {
            this.monto = monto;
        }

        public LocalDate getFecha() {
            return fecha;
        }

        public void setFecha(LocalDate fecha) {
            this.fecha = fecha;
        }

        public String getUrlComprobante() {
            return urlComprobante;
        }

        public void setUrlComprobante(String urlComprobante) {
            this.urlComprobante = urlComprobante;
        }
    }

    public static class EventoResumenDTO {
        private Long id;
        private String tipo;
        private String descripcion;
        private LocalDate fecha;
        private String url;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public LocalDate getFecha() {
            return fecha;
        }

        public void setFecha(LocalDate fecha) {
            this.fecha = fecha;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
