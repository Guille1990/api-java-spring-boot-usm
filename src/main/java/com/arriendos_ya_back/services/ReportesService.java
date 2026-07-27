package com.arriendos_ya_back.services;

import com.arriendos_ya_back.dto.ReportePropiedadMensualDTO;
import com.arriendos_ya_back.models.TipoMovimiento;
import com.arriendos_ya_back.models.arriendo;
import com.arriendos_ya_back.models.evento;
import com.arriendos_ya_back.models.movimiento;
import com.arriendos_ya_back.models.propiedad;
import com.arriendos_ya_back.repositories.ArriendosRepository;
import com.arriendos_ya_back.repositories.EventosRepository;
import com.arriendos_ya_back.repositories.MovimientosRepository;
import com.arriendos_ya_back.repositories.PropiedadesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
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
}
