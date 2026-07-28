package com.arriendos_ya_back.services;

import com.arriendos_ya_back.dto.ReportePropietarioMensualDTO;
import com.arriendos_ya_back.dto.ReportePropietariosMensualDTO;
import com.arriendos_ya_back.dto.ReportePropiedadMensualDTO;
import com.arriendos_ya_back.models.TipoMovimiento;
import com.arriendos_ya_back.models.arriendo;
import com.arriendos_ya_back.models.evento;
import com.arriendos_ya_back.models.movimiento;
import com.arriendos_ya_back.models.propiedad;
import com.arriendos_ya_back.models.propietario;
import com.arriendos_ya_back.repositories.ArriendosRepository;
import com.arriendos_ya_back.repositories.EventosRepository;
import com.arriendos_ya_back.repositories.MovimientosRepository;
import com.arriendos_ya_back.repositories.PropiedadesRepository;
import com.arriendos_ya_back.repositories.PropietariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReportesService {

    private static final ZoneId ZONA_REPORTE = ZoneId.of("America/Santiago");

    @Autowired
    private PropiedadesRepository propiedadesRepository;

    @Autowired
    private MovimientosRepository movimientosRepository;

    @Autowired
    private ArriendosRepository arriendosRepository;

    @Autowired
    private EventosRepository eventosRepository;

    @Autowired
    private PropietariosRepository propietariosRepository;

    public Optional<ReportePropiedadMensualDTO> generarReporteMensualPorPropiedad(Long propiedadId, Integer anio, Integer mes) {
        Optional<propiedad> propiedadOpt = propiedadesRepository.findById(propiedadId);
        if (propiedadOpt.isEmpty()) {
            return Optional.empty();
        }

        YearMonth yearMonth = YearMonth.of(anio, mes);
        LocalDate inicioMes = yearMonth.atDay(1);
        LocalDate finMes = yearMonth.atEndOfMonth();

        ZonedDateTime inicioRango = inicioMes.atStartOfDay(ZONA_REPORTE);
        ZonedDateTime finRangoExclusivo = inicioMes.plusMonths(1).atStartOfDay(ZONA_REPORTE);

        propiedad propiedad = propiedadOpt.get();

        Double ingresos = movimientosRepository.sumMontoByPropiedadAndTipoAndFechaRango(
                propiedadId,
                TipoMovimiento.INGRESO,
                inicioRango,
                finRangoExclusivo);

        Double egresos = movimientosRepository.sumMontoByPropiedadAndTipoAndFechaRango(
                propiedadId,
                TipoMovimiento.EGRESO,
                inicioRango,
                finRangoExclusivo);

        double totalIngresos = ingresos == null ? 0.0 : ingresos;
        double totalEgresos = egresos == null ? 0.0 : egresos;

        List<movimiento> movimientosMes = movimientosRepository.findByPropiedadIdAndFechaRango(
                propiedadId,
                inicioRango,
                finRangoExclusivo);

        List<evento> eventosMes = eventosRepository.findByPropiedadIdAndFechaRango(
                propiedadId,
                inicioRango,
                finRangoExclusivo);

        List<arriendo> arriendosActivosMes = arriendosRepository.findActivosEnPeriodo(
                propiedadId,
                inicioMes,
                finMes);

        int diasTotalesMes = yearMonth.lengthOfMonth();
        int diasOcupados = calcularDiasOcupados(arriendosActivosMes, inicioMes, finMes);
        double porcentajeOcupacion = diasTotalesMes == 0 ? 0.0 : (diasOcupados * 100.0) / diasTotalesMes;

        ReportePropiedadMensualDTO reporte = new ReportePropiedadMensualDTO();
        reporte.setPropiedadId(propiedad.getId());
        reporte.setDireccion(propiedad.getDireccion());
        reporte.setComuna(propiedad.getComuna());
        reporte.setCiudad(propiedad.getCiudad());
        reporte.setRegion(propiedad.getRegion());
        reporte.setAnio(anio);
        reporte.setMes(mes);

        reporte.setTotalIngresos(totalIngresos);
        reporte.setTotalEgresos(totalEgresos);
        reporte.setBalance(totalIngresos - totalEgresos);

        reporte.setDiasTotalesMes(diasTotalesMes);
        reporte.setDiasOcupados(diasOcupados);
        reporte.setPorcentajeOcupacion(porcentajeOcupacion);

        reporte.setMovimientos(movimientosMes.stream().map(this::mapMovimiento).collect(Collectors.toList()));
        reporte.setEventos(eventosMes.stream().map(this::mapEvento).collect(Collectors.toList()));

        return Optional.of(reporte);
    }

    public Optional<ReportePropietariosMensualDTO> generarReporteAnualPorPropietarios(Integer anio, String propietarioRut) {
        LocalDate inicioAnio = LocalDate.of(anio, 1, 1);

        ZonedDateTime inicioRango = inicioAnio.atStartOfDay(ZONA_REPORTE);
        ZonedDateTime finRangoExclusivo = inicioAnio.plusYears(1).atStartOfDay(ZONA_REPORTE);

        String rutLimpio = propietarioRut == null ? null : propietarioRut.trim();
        if (rutLimpio != null && rutLimpio.isEmpty()) {
            rutLimpio = null;
        }

        List<propiedad> propiedades;
        propietario propietarioFiltrado = null;
        if (rutLimpio == null) {
            propiedades = propiedadesRepository.findAllByOrderByIdAsc();
        } else {
            Optional<propietario> propietarioOpt = propietariosRepository.findById(rutLimpio);
            if (propietarioOpt.isEmpty()) {
                return Optional.empty();
            }
            propietarioFiltrado = propietarioOpt.get();
            propiedades = propiedadesRepository.findByPropietarioRutOrderByIdAsc(rutLimpio);
        }

        Map<String, ReportePropietarioMensualDTO> reportesPorPropietario = new LinkedHashMap<>();
        Map<String, AcumuladorMeses> acumuladoresPorPropietario = new HashMap<>();
        Map<Long, String> rutPorPropiedad = new HashMap<>();
        Map<Long, ReportePropietarioMensualDTO.PropiedadResumenDTO> resumenPorPropiedadId = new HashMap<>();
        List<ReportePropietarioMensualDTO> reportes = new ArrayList<>();

        for (propiedad propiedad : propiedades) {
            propietario propietario = propiedad.getPropietario();
            String rut = propietario == null ? "SIN_RUT" : propietario.getRut();

            ReportePropietarioMensualDTO reportePropietario = reportesPorPropietario.get(rut);
            if (reportePropietario == null) {
                reportePropietario = new ReportePropietarioMensualDTO();
                reportePropietario.setPropietarioRut(propietario == null ? null : propietario.getRut());
                reportePropietario.setPropietarioNombreCompleto(construirNombrePropietario(propietario));
                reportePropietario.setAnio(anio);
                reportesPorPropietario.put(rut, reportePropietario);
                acumuladoresPorPropietario.put(rut, new AcumuladorMeses());
                reportes.add(reportePropietario);
            }

            ReportePropietarioMensualDTO.PropiedadResumenDTO propiedadResumen =
                    new ReportePropietarioMensualDTO.PropiedadResumenDTO();
            propiedadResumen.setPropiedadId(propiedad.getId());
            propiedadResumen.setDireccion(propiedad.getDireccion());
            propiedadResumen.setComuna(propiedad.getComuna());
            propiedadResumen.setCiudad(propiedad.getCiudad());
            propiedadResumen.setRegion(propiedad.getRegion());
            propiedadResumen.setTotalIngresos(0.0);
            propiedadResumen.setTotalEgresos(0.0);
            propiedadResumen.setBalance(0.0);

            reportePropietario.getPropiedades().add(propiedadResumen);
            reportePropietario.setCantidadPropiedades(reportePropietario.getCantidadPropiedades() + 1);

            rutPorPropiedad.put(propiedad.getId(), rut);
            resumenPorPropiedadId.put(propiedad.getId(), propiedadResumen);
        }

        if (propietarioFiltrado != null && reportes.isEmpty()) {
            ReportePropietarioMensualDTO reportePropietario = new ReportePropietarioMensualDTO();
            reportePropietario.setPropietarioRut(propietarioFiltrado.getRut());
            reportePropietario.setPropietarioNombreCompleto(construirNombrePropietario(propietarioFiltrado));
            reportePropietario.setAnio(anio);
            reportes.add(reportePropietario);
            reportesPorPropietario.put(propietarioFiltrado.getRut(), reportePropietario);
            acumuladoresPorPropietario.put(propietarioFiltrado.getRut(), new AcumuladorMeses());
        }

        AcumuladorMeses acumuladorGlobal = new AcumuladorMeses();

        if (!propiedades.isEmpty()) {
            List<Long> propiedadIds = propiedades.stream().map(propiedad::getId).collect(Collectors.toList());
            List<movimiento> movimientosAnio = movimientosRepository.findByPropiedadIdsAndFechaRango(
                    propiedadIds,
                    inicioRango,
                    finRangoExclusivo);

            for (movimiento movimiento : movimientosAnio) {
                if (movimiento.getFecha() == null || movimiento.getPropiedad() == null) {
                    continue;
                }

                Long propiedadId = movimiento.getPropiedad().getId();
                String rut = rutPorPropiedad.get(propiedadId);
                if (rut == null) {
                    continue;
                }

                int mesMovimiento = movimiento.getFecha().withZoneSameInstant(ZONA_REPORTE).getMonthValue();
                double monto = movimiento.getMonto();
                TipoMovimiento tipo = movimiento.getTipo();

                acumuladorGlobal.agregar(tipo, mesMovimiento, monto);

                AcumuladorMeses acumuladorPropietario = acumuladoresPorPropietario.get(rut);
                if (acumuladorPropietario != null) {
                    acumuladorPropietario.agregar(tipo, mesMovimiento, monto);
                }

                ReportePropietarioMensualDTO.PropiedadResumenDTO propiedadResumen = resumenPorPropiedadId.get(propiedadId);
                if (propiedadResumen != null) {
                    if (tipo == TipoMovimiento.INGRESO) {
                        propiedadResumen.setTotalIngresos(propiedadResumen.getTotalIngresos() + monto);
                    } else if (tipo == TipoMovimiento.EGRESO) {
                        propiedadResumen.setTotalEgresos(propiedadResumen.getTotalEgresos() + monto);
                    }
                    propiedadResumen.setBalance(propiedadResumen.getTotalIngresos() - propiedadResumen.getTotalEgresos());
                }
            }
        }

        for (Map.Entry<String, ReportePropietarioMensualDTO> entry : reportesPorPropietario.entrySet()) {
            String rut = entry.getKey();
            ReportePropietarioMensualDTO reporte = entry.getValue();
            AcumuladorMeses acumulador = acumuladoresPorPropietario.getOrDefault(rut, new AcumuladorMeses());

            reporte.setResumenMensual(construirResumenMensualPropietario(acumulador));
            reporte.setTotalIngresos(acumulador.totalIngresos());
            reporte.setTotalEgresos(acumulador.totalEgresos());
            reporte.setBalance(reporte.getTotalIngresos() - reporte.getTotalEgresos());
        }

        ReportePropietariosMensualDTO respuesta = new ReportePropietariosMensualDTO();
        respuesta.setAnio(anio);
        respuesta.setPropietarioRutFiltro(rutLimpio);
        respuesta.setPropietarios(reportes);
        respuesta.setCantidadPropietarios(reportes.size());
        respuesta.setResumenMensualGlobal(construirResumenMensualGlobal(acumuladorGlobal));
        respuesta.setTotalIngresos(acumuladorGlobal.totalIngresos());
        respuesta.setTotalEgresos(acumuladorGlobal.totalEgresos());
        respuesta.setBalance(respuesta.getTotalIngresos() - respuesta.getTotalEgresos());

        return Optional.of(respuesta);
    }

    private List<ReportePropietarioMensualDTO.ResumenMesDTO> construirResumenMensualPropietario(AcumuladorMeses acumulador) {
        List<ReportePropietarioMensualDTO.ResumenMesDTO> resumen = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            ReportePropietarioMensualDTO.ResumenMesDTO mes = new ReportePropietarioMensualDTO.ResumenMesDTO();
            mes.setMes(i + 1);
            mes.setTotalIngresos(acumulador.ingresos[i]);
            mes.setTotalEgresos(acumulador.egresos[i]);
            mes.setBalance(mes.getTotalIngresos() - mes.getTotalEgresos());
            resumen.add(mes);
        }
        return resumen;
    }

    private List<ReportePropietariosMensualDTO.ResumenMesDTO> construirResumenMensualGlobal(AcumuladorMeses acumulador) {
        List<ReportePropietariosMensualDTO.ResumenMesDTO> resumen = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            ReportePropietariosMensualDTO.ResumenMesDTO mes = new ReportePropietariosMensualDTO.ResumenMesDTO();
            mes.setMes(i + 1);
            mes.setTotalIngresos(acumulador.ingresos[i]);
            mes.setTotalEgresos(acumulador.egresos[i]);
            mes.setBalance(mes.getTotalIngresos() - mes.getTotalEgresos());
            resumen.add(mes);
        }
        return resumen;
    }

    private int calcularDiasOcupados(List<arriendo> arriendos, LocalDate inicioMes, LocalDate finMes) {
        Set<LocalDate> diasOcupados = new HashSet<>();

        for (arriendo arriendo : arriendos) {
            LocalDate inicioArriendo = arriendo.getFechaInicio();
            LocalDate finArriendo = arriendo.getFechaTermino() == null ? finMes : arriendo.getFechaTermino();

            LocalDate inicioReal = inicioArriendo.isBefore(inicioMes) ? inicioMes : inicioArriendo;
            LocalDate finReal = finArriendo.isAfter(finMes) ? finMes : finArriendo;

            if (finReal.isBefore(inicioReal)) {
                continue;
            }

            for (LocalDate dia = inicioReal; !dia.isAfter(finReal); dia = dia.plusDays(1)) {
                diasOcupados.add(dia);
            }
        }

        return diasOcupados.size();
    }

    private ReportePropiedadMensualDTO.MovimientoResumenDTO mapMovimiento(movimiento movimiento) {
        ReportePropiedadMensualDTO.MovimientoResumenDTO dto = new ReportePropiedadMensualDTO.MovimientoResumenDTO();
        dto.setId(movimiento.getId());
        dto.setConcepto(movimiento.getConcepto());
        dto.setTipo(movimiento.getTipo());
        dto.setMonto(movimiento.getMonto());
        dto.setFecha(movimiento.getFecha() == null ? null : movimiento.getFecha().withZoneSameInstant(ZONA_REPORTE).toLocalDate());
        dto.setUrlComprobante(movimiento.getUrlComprobante());
        return dto;
    }

    private ReportePropiedadMensualDTO.EventoResumenDTO mapEvento(evento evento) {
        ReportePropiedadMensualDTO.EventoResumenDTO dto = new ReportePropiedadMensualDTO.EventoResumenDTO();
        dto.setId(evento.getId());
        dto.setTipo(evento.getTipo());
        dto.setDescripcion(evento.getDescripcion());
        dto.setFecha(evento.getFecha() == null ? null : evento.getFecha().withZoneSameInstant(ZONA_REPORTE).toLocalDate());
        dto.setUrl(evento.getUrl());
        return dto;
    }

    private String construirNombrePropietario(propietario propietario) {
        if (propietario == null) {
            return "Sin propietario";
        }

        String nombre = propietario.getNombre() == null ? "" : propietario.getNombre().trim();
        String apellido = propietario.getApellido() == null ? "" : propietario.getApellido().trim();
        String nombreCompleto = (nombre + " " + apellido).trim();

        if (nombreCompleto.isEmpty()) {
            return "Sin nombre";
        }

        return nombreCompleto;
    }

    private static class AcumuladorMeses {
        private final double[] ingresos = new double[12];
        private final double[] egresos = new double[12];

        void agregar(TipoMovimiento tipo, int mes, double monto) {
            if (mes < 1 || mes > 12 || tipo == null) {
                return;
            }

            int indice = mes - 1;
            if (tipo == TipoMovimiento.INGRESO) {
                ingresos[indice] += monto;
            } else if (tipo == TipoMovimiento.EGRESO) {
                egresos[indice] += monto;
            }
        }

        double totalIngresos() {
            double total = 0.0;
            for (double ingreso : ingresos) {
                total += ingreso;
            }
            return total;
        }

        double totalEgresos() {
            double total = 0.0;
            for (double egreso : egresos) {
                total += egreso;
            }
            return total;
        }
    }
}
