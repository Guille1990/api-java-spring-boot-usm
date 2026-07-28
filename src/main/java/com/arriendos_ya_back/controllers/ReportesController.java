package com.arriendos_ya_back.controllers;

import com.arriendos_ya_back.dto.EnviarReporteRequest;
import com.arriendos_ya_back.dto.ReportePropietariosMensualDTO;
import com.arriendos_ya_back.dto.ReportePropiedadMensualDTO;
import com.arriendos_ya_back.services.ReporteEmailService;
import com.arriendos_ya_back.services.ReportePropietariosExportService;
import com.arriendos_ya_back.services.ReportesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReportesController {

    @Autowired
    private ReportesService reportesService;

    @Autowired
    private ReporteEmailService reporteEmailService;

    @Autowired
    private ReportePropietariosExportService reportePropietariosExportService;

    @GetMapping("/propiedad/{propiedadId}/mensual")
    public ResponseEntity<ReportePropiedadMensualDTO> obtenerReporteMensualPorPropiedad(
            @PathVariable Long propiedadId,
            @RequestParam Integer anio,
            @RequestParam Integer mes) {

        if (mes < 1 || mes > 12) {
            return ResponseEntity.badRequest().build();
        }

        Optional<ReportePropiedadMensualDTO> reporteOpt =
                reportesService.generarReporteMensualPorPropiedad(propiedadId, anio, mes);

        return reporteOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/propietarios/anual")
    public ResponseEntity<ReportePropietariosMensualDTO> obtenerReporteAnualPorPropietarios(
            @RequestParam Integer anio,
            @RequestParam(required = false) String propietarioRut) {

        Optional<ReportePropietariosMensualDTO> reporteOpt =
                reportesService.generarReporteAnualPorPropietarios(anio, propietarioRut);

        return reporteOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/propietarios/anual/exportar/pdf")
    public ResponseEntity<byte[]> exportarReporteAnualPorPropietariosPdf(
            @RequestParam Integer anio,
            @RequestParam(required = false) String propietarioRut) {

        Optional<byte[]> contenidoOpt = reportePropietariosExportService.exportarPdf(anio, propietarioRut);
        if (contenidoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String propietarioNombreArchivo = propietarioRut == null || propietarioRut.trim().isEmpty()
                ? "todos"
                : propietarioRut.trim();

        String nombreArchivo = "reporte-propietarios-anual-" + anio + "-" + propietarioNombreArchivo + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(contenidoOpt.get());
    }

    @GetMapping("/propietarios/anual/exportar/excel")
    public ResponseEntity<byte[]> exportarReporteAnualPorPropietariosExcel(
            @RequestParam Integer anio,
            @RequestParam(required = false) String propietarioRut) {

        Optional<byte[]> contenidoOpt = reportePropietariosExportService.exportarExcel(anio, propietarioRut);
        if (contenidoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String propietarioNombreArchivo = propietarioRut == null || propietarioRut.trim().isEmpty()
                ? "todos"
                : propietarioRut.trim();

        String nombreArchivo = "reporte-propietarios-anual-" + anio + "-" + propietarioNombreArchivo + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(contenidoOpt.get());
    }

    @PostMapping("/propiedad/{propiedadId}/mensual/enviar")
    public ResponseEntity<Map<String, String>> enviarReporteMensualPorCorreo(
            @PathVariable Long propiedadId,
            @RequestParam Integer anio,
            @RequestParam Integer mes,
            @RequestBody EnviarReporteRequest request) {

        if (mes < 1 || mes > 12) {
            return ResponseEntity.badRequest().body(Map.of("error", "El mes debe estar entre 1 y 12"));
        }

        Optional<String> error = reporteEmailService.enviarReporteMensual(
                propiedadId,
                anio,
                mes,
                request == null ? null : request.getDestinatarios());

        if (error.isPresent()) {
            String mensaje = error.get();
            if (mensaje.contains("No se encontro la propiedad")) {
                return ResponseEntity.status(404).body(Map.of("error", mensaje));
            }
            return ResponseEntity.badRequest().body(Map.of("error", mensaje));
        }

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("message", "Reporte enviado correctamente");
        return ResponseEntity.ok(respuesta);
    }
}
