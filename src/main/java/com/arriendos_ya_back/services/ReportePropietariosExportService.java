package com.arriendos_ya_back.services;

import com.arriendos_ya_back.dto.ReportePropietarioMensualDTO;
import com.arriendos_ya_back.dto.ReportePropietariosMensualDTO;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ReportePropietariosExportService {

    @Autowired
    private ReportesService reportesService;

    public Optional<byte[]> exportarPdf(Integer anio, String propietarioRut) {
        Optional<ReportePropietariosMensualDTO> reporteOpt =
                reportesService.generarReporteAnualPorPropietarios(anio, propietarioRut);

        if (reporteOpt.isEmpty()) {
            return Optional.empty();
        }

        String html = construirHtmlReporte(reporteOpt.get());
        return Optional.of(generarPdfDesdeHtml(html));
    }

    public Optional<byte[]> exportarExcel(Integer anio, String propietarioRut) {
        Optional<ReportePropietariosMensualDTO> reporteOpt =
                reportesService.generarReporteAnualPorPropietarios(anio, propietarioRut);

        if (reporteOpt.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(generarExcel(reporteOpt.get()));
    }

    private String construirHtmlReporte(ReportePropietariosMensualDTO reporte) {
        DecimalFormat formatoMonto = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));

        StringBuilder resumenGlobalMensualRows = new StringBuilder();
        for (ReportePropietariosMensualDTO.ResumenMesDTO mes : reporte.getResumenMensualGlobal()) {
            resumenGlobalMensualRows.append("<tr>")
                    .append("<td>").append(mes.getMes()).append("</td>")
                    .append("<td style='text-align:right;'>").append(formatoMonto.format(mes.getTotalIngresos())).append("</td>")
                    .append("<td style='text-align:right;'>").append(formatoMonto.format(mes.getTotalEgresos())).append("</td>")
                    .append("<td style='text-align:right;'>").append(formatoMonto.format(mes.getBalance())).append("</td>")
                    .append("</tr>");
        }

        if (resumenGlobalMensualRows.length() == 0) {
            resumenGlobalMensualRows.append("<tr><td colspan='4'>Sin datos mensuales</td></tr>");
        }

        StringBuilder propietariosRows = new StringBuilder();
        for (ReportePropietarioMensualDTO propietario : reporte.getPropietarios()) {
            propietariosRows.append("<tr>")
                    .append("<td>").append(escaparHtml(propietario.getPropietarioRut())).append("</td>")
                    .append("<td>").append(escaparHtml(propietario.getPropietarioNombreCompleto())).append("</td>")
                    .append("<td style='text-align:right;'>").append(propietario.getCantidadPropiedades()).append("</td>")
                    .append("<td style='text-align:right;'>").append(formatoMonto.format(propietario.getTotalIngresos())).append("</td>")
                    .append("<td style='text-align:right;'>").append(formatoMonto.format(propietario.getTotalEgresos())).append("</td>")
                    .append("<td style='text-align:right;'>").append(formatoMonto.format(propietario.getBalance())).append("</td>")
                    .append("</tr>");
        }

        if (propietariosRows.length() == 0) {
            propietariosRows.append("<tr><td colspan='6'>Sin propietarios para el periodo</td></tr>");
        }

        StringBuilder propietariosMensualRows = new StringBuilder();
        for (ReportePropietarioMensualDTO propietario : reporte.getPropietarios()) {
            for (ReportePropietarioMensualDTO.ResumenMesDTO mes : propietario.getResumenMensual()) {
                propietariosMensualRows.append("<tr>")
                        .append("<td>").append(escaparHtml(propietario.getPropietarioRut())).append("</td>")
                        .append("<td>").append(escaparHtml(propietario.getPropietarioNombreCompleto())).append("</td>")
                        .append("<td style='text-align:right;'>").append(mes.getMes()).append("</td>")
                        .append("<td style='text-align:right;'>").append(formatoMonto.format(mes.getTotalIngresos())).append("</td>")
                        .append("<td style='text-align:right;'>").append(formatoMonto.format(mes.getTotalEgresos())).append("</td>")
                        .append("<td style='text-align:right;'>").append(formatoMonto.format(mes.getBalance())).append("</td>")
                        .append("</tr>");
            }
        }

        if (propietariosMensualRows.length() == 0) {
            propietariosMensualRows.append("<tr><td colspan='6'>Sin desglose mensual por propietario</td></tr>");
        }

        StringBuilder detalleRows = new StringBuilder();
        for (ReportePropietarioMensualDTO propietario : reporte.getPropietarios()) {
            List<ReportePropietarioMensualDTO.PropiedadResumenDTO> propiedades = propietario.getPropiedades();
            for (ReportePropietarioMensualDTO.PropiedadResumenDTO propiedad : propiedades) {
                detalleRows.append("<tr>")
                        .append("<td>").append(escaparHtml(propietario.getPropietarioRut())).append("</td>")
                        .append("<td>").append(escaparHtml(propietario.getPropietarioNombreCompleto())).append("</td>")
                        .append("<td style='text-align:right;'>").append(propiedad.getPropiedadId() == null ? "-" : propiedad.getPropiedadId()).append("</td>")
                        .append("<td>").append(escaparHtml(propiedad.getDireccion())).append("</td>")
                        .append("<td>").append(escaparHtml(propiedad.getComuna())).append("</td>")
                        .append("<td>").append(escaparHtml(propiedad.getCiudad())).append("</td>")
                        .append("<td>").append(escaparHtml(propiedad.getRegion())).append("</td>")
                        .append("<td style='text-align:right;'>").append(formatoMonto.format(propiedad.getTotalIngresos())).append("</td>")
                        .append("<td style='text-align:right;'>").append(formatoMonto.format(propiedad.getTotalEgresos())).append("</td>")
                        .append("<td style='text-align:right;'>").append(formatoMonto.format(propiedad.getBalance())).append("</td>")
                        .append("</tr>");
            }
        }

        if (detalleRows.length() == 0) {
            detalleRows.append("<tr><td colspan='10'>Sin propiedades para el periodo</td></tr>");
        }

        return "<html><head><meta charset='UTF-8'/>" +
                "<style>" +
                "body{font-family:Arial,sans-serif;color:#1f2937;margin:20px;}" +
                "h1{font-size:24px;margin-bottom:4px;}" +
                "h2{font-size:16px;margin-top:24px;border-bottom:1px solid #e5e7eb;padding-bottom:4px;}" +
                ".meta{color:#6b7280;margin-bottom:14px;}" +
                ".cards{display:flex;gap:12px;flex-wrap:wrap;}" +
                ".card{border:1px solid #e5e7eb;border-radius:8px;padding:10px 12px;min-width:190px;}" +
                ".k{font-size:12px;color:#6b7280;}" +
                ".v{font-size:22px;font-weight:700;margin-top:2px;}" +
                "table{width:100%;border-collapse:collapse;margin-top:10px;}" +
                "th,td{border:1px solid #e5e7eb;padding:8px;font-size:12px;}" +
                "th{background:#f3f4f6;text-align:left;}" +
                "</style></head><body>" +
                "<h1>Reporte Anual por Propietarios</h1>" +
                "<div class='meta'>Anio " + reporte.getAnio() +
                " | Filtro propietario: " + escaparHtml(reporte.getPropietarioRutFiltro() == null ? "TODOS" : reporte.getPropietarioRutFiltro()) +
                "</div>" +
                "<div class='cards'>" +
                "<div class='card'><div class='k'>Propietarios</div><div class='v'>" + reporte.getCantidadPropietarios() + "</div></div>" +
                "<div class='card'><div class='k'>Ingresos</div><div class='v'>" + formatoMonto.format(reporte.getTotalIngresos()) + "</div></div>" +
                "<div class='card'><div class='k'>Egresos</div><div class='v'>" + formatoMonto.format(reporte.getTotalEgresos()) + "</div></div>" +
                "<div class='card'><div class='k'>Balance</div><div class='v'>" + formatoMonto.format(reporte.getBalance()) + "</div></div>" +
                "</div>" +
                "<h2>Resumen por Propietario</h2>" +
                "<table><thead><tr><th>RUT</th><th>Nombre</th><th>Propiedades</th><th>Ingresos</th><th>Egresos</th><th>Balance</th></tr></thead><tbody>" +
                propietariosRows +
                "</tbody></table>" +
                "<h2>Desglose Mensual Global</h2>" +
                "<table><thead><tr><th>Mes</th><th>Ingresos</th><th>Egresos</th><th>Balance</th></tr></thead><tbody>" +
                resumenGlobalMensualRows +
                "</tbody></table>" +
                "<h2>Desglose Mensual por Propietario</h2>" +
                "<table><thead><tr><th>RUT</th><th>Nombre</th><th>Mes</th><th>Ingresos</th><th>Egresos</th><th>Balance</th></tr></thead><tbody>" +
                propietariosMensualRows +
                "</tbody></table>" +
                "<h2>Detalle por Propiedad</h2>" +
                "<table><thead><tr><th>RUT</th><th>Nombre</th><th>ID</th><th>Direccion</th><th>Comuna</th><th>Ciudad</th><th>Region</th><th>Ingresos</th><th>Egresos</th><th>Balance</th></tr></thead><tbody>" +
                detalleRows +
                "</tbody></table>" +
                "</body></html>";
    }

    private byte[] generarPdfDesdeHtml(String html) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(output);
            builder.run();
            return output.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("No fue posible generar el PDF del reporte", e);
        }
    }

    private byte[] generarExcel(ReportePropietariosMensualDTO reporte) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            CellStyle estiloHeader = workbook.createCellStyle();
            Font fuenteHeader = workbook.createFont();
            fuenteHeader.setBold(true);
            estiloHeader.setFont(fuenteHeader);

            Sheet resumen = workbook.createSheet("Resumen");
            int fila = 0;
            fila = crearFilaTitulo(resumen, fila, "Reporte Anual por Propietarios");
            fila = crearFilaClaveValor(resumen, fila, "Anio", String.valueOf(reporte.getAnio()));
            fila = crearFilaClaveValor(resumen, fila, "Filtro propietario", reporte.getPropietarioRutFiltro() == null ? "TODOS" : reporte.getPropietarioRutFiltro());
            fila = crearFilaClaveValor(resumen, fila, "Cantidad propietarios", String.valueOf(reporte.getCantidadPropietarios()));
            fila = crearFilaClaveValor(resumen, fila, "Total ingresos", String.valueOf(reporte.getTotalIngresos()));
            fila = crearFilaClaveValor(resumen, fila, "Total egresos", String.valueOf(reporte.getTotalEgresos()));
            fila = crearFilaClaveValor(resumen, fila, "Balance", String.valueOf(reporte.getBalance()));
            fila++;

            Row cabeceraResumen = resumen.createRow(fila++);
            String[] columnasResumen = {"RUT", "Nombre", "Propiedades", "Ingresos", "Egresos", "Balance"};
            for (int i = 0; i < columnasResumen.length; i++) {
                Cell cell = cabeceraResumen.createCell(i);
                cell.setCellValue(columnasResumen[i]);
                cell.setCellStyle(estiloHeader);
            }

            for (ReportePropietarioMensualDTO propietario : reporte.getPropietarios()) {
                Row row = resumen.createRow(fila++);
                row.createCell(0).setCellValue(valorSeguro(propietario.getPropietarioRut()));
                row.createCell(1).setCellValue(valorSeguro(propietario.getPropietarioNombreCompleto()));
                row.createCell(2).setCellValue(propietario.getCantidadPropiedades());
                row.createCell(3).setCellValue(propietario.getTotalIngresos());
                row.createCell(4).setCellValue(propietario.getTotalEgresos());
                row.createCell(5).setCellValue(propietario.getBalance());
            }

            for (int i = 0; i < columnasResumen.length; i++) {
                resumen.autoSizeColumn(i);
            }

            Sheet mensualGlobal = workbook.createSheet("Mensual Global");
            int filaMensualGlobal = 0;
            Row cabeceraMensualGlobal = mensualGlobal.createRow(filaMensualGlobal++);
            String[] columnasMensualGlobal = {"Mes", "Ingresos", "Egresos", "Balance"};
            for (int i = 0; i < columnasMensualGlobal.length; i++) {
                Cell cell = cabeceraMensualGlobal.createCell(i);
                cell.setCellValue(columnasMensualGlobal[i]);
                cell.setCellStyle(estiloHeader);
            }

            for (ReportePropietariosMensualDTO.ResumenMesDTO mes : reporte.getResumenMensualGlobal()) {
                Row row = mensualGlobal.createRow(filaMensualGlobal++);
                row.createCell(0).setCellValue(mes.getMes());
                row.createCell(1).setCellValue(mes.getTotalIngresos());
                row.createCell(2).setCellValue(mes.getTotalEgresos());
                row.createCell(3).setCellValue(mes.getBalance());
            }

            for (int i = 0; i < columnasMensualGlobal.length; i++) {
                mensualGlobal.autoSizeColumn(i);
            }

            Sheet mensualPropietario = workbook.createSheet("Mensual Propietario");
            int filaMensualPropietario = 0;
            Row cabeceraMensualPropietario = mensualPropietario.createRow(filaMensualPropietario++);
            String[] columnasMensualPropietario = {"RUT", "Nombre", "Mes", "Ingresos", "Egresos", "Balance"};
            for (int i = 0; i < columnasMensualPropietario.length; i++) {
                Cell cell = cabeceraMensualPropietario.createCell(i);
                cell.setCellValue(columnasMensualPropietario[i]);
                cell.setCellStyle(estiloHeader);
            }

            for (ReportePropietarioMensualDTO propietario : reporte.getPropietarios()) {
                for (ReportePropietarioMensualDTO.ResumenMesDTO mes : propietario.getResumenMensual()) {
                    Row row = mensualPropietario.createRow(filaMensualPropietario++);
                    row.createCell(0).setCellValue(valorSeguro(propietario.getPropietarioRut()));
                    row.createCell(1).setCellValue(valorSeguro(propietario.getPropietarioNombreCompleto()));
                    row.createCell(2).setCellValue(mes.getMes());
                    row.createCell(3).setCellValue(mes.getTotalIngresos());
                    row.createCell(4).setCellValue(mes.getTotalEgresos());
                    row.createCell(5).setCellValue(mes.getBalance());
                }
            }

            for (int i = 0; i < columnasMensualPropietario.length; i++) {
                mensualPropietario.autoSizeColumn(i);
            }

            Sheet detalle = workbook.createSheet("Detalle Propiedades");
            int filaDetalle = 0;
            Row cabeceraDetalle = detalle.createRow(filaDetalle++);
            String[] columnasDetalle = {
                    "RUT", "Nombre", "Propiedad ID", "Direccion", "Comuna", "Ciudad", "Region", "Ingresos", "Egresos", "Balance"
            };
            for (int i = 0; i < columnasDetalle.length; i++) {
                Cell cell = cabeceraDetalle.createCell(i);
                cell.setCellValue(columnasDetalle[i]);
                cell.setCellStyle(estiloHeader);
            }

            for (ReportePropietarioMensualDTO propietario : reporte.getPropietarios()) {
                for (ReportePropietarioMensualDTO.PropiedadResumenDTO propiedad : propietario.getPropiedades()) {
                    Row row = detalle.createRow(filaDetalle++);
                    row.createCell(0).setCellValue(valorSeguro(propietario.getPropietarioRut()));
                    row.createCell(1).setCellValue(valorSeguro(propietario.getPropietarioNombreCompleto()));
                    row.createCell(2).setCellValue(propiedad.getPropiedadId() == null ? 0 : propiedad.getPropiedadId());
                    row.createCell(3).setCellValue(valorSeguro(propiedad.getDireccion()));
                    row.createCell(4).setCellValue(valorSeguro(propiedad.getComuna()));
                    row.createCell(5).setCellValue(valorSeguro(propiedad.getCiudad()));
                    row.createCell(6).setCellValue(valorSeguro(propiedad.getRegion()));
                    row.createCell(7).setCellValue(propiedad.getTotalIngresos());
                    row.createCell(8).setCellValue(propiedad.getTotalEgresos());
                    row.createCell(9).setCellValue(propiedad.getBalance());
                }
            }

            for (int i = 0; i < columnasDetalle.length; i++) {
                detalle.autoSizeColumn(i);
            }

            workbook.write(output);
            return output.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("No fue posible generar el archivo Excel del reporte", e);
        }
    }

    private int crearFilaTitulo(Sheet sheet, int fila, String titulo) {
        Row row = sheet.createRow(fila);
        row.createCell(0).setCellValue(titulo);
        return fila + 1;
    }

    private int crearFilaClaveValor(Sheet sheet, int fila, String clave, String valor) {
        Row row = sheet.createRow(fila);
        row.createCell(0).setCellValue(clave);
        row.createCell(1).setCellValue(valorSeguro(valor));
        return fila + 1;
    }

    private String valorSeguro(String valor) {
        return valor == null ? "" : valor;
    }

    private String escaparHtml(String valor) {
        if (valor == null) {
            return "";
        }

        return valor
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}