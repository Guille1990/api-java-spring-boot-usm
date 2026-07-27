package com.arriendos_ya_back.services;

import com.arriendos_ya_back.dto.ReportePropiedadMensualDTO;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.lang.Nullable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReporteEmailService {

    @Autowired(required = false)
    @Nullable
    private JavaMailSender javaMailSender;

    @Autowired
    private ReportesService reportesService;

    @Value("${app.mail.from:no-reply@arriendosya.local}")
    private String fromEmail;

    public Optional<String> enviarReporteMensual(Long propiedadId, Integer anio, Integer mes, List<String> destinatarios) {
        if (destinatarios == null || destinatarios.isEmpty()) {
            return Optional.of("Debe indicar al menos un destinatario");
        }

        JavaMailSender sender = javaMailSender;
        if (sender == null) {
            return Optional.of("El servicio de correo no esta configurado (faltan variables MAIL_*)");
        }

        List<String> correosValidos = new ArrayList<>();
        for (String correo : destinatarios) {
            if (correo == null) {
                continue;
            }

            String correoLimpio = correo.trim();
            if (!correoLimpio.isEmpty()) {
                correosValidos.add(correoLimpio);
            }
        }

        String[] correos = correosValidos.toArray(new String[0]);

        if (correos.length == 0) {
            return Optional.of("Debe indicar al menos un destinatario valido");
        }

        Optional<ReportePropiedadMensualDTO> reporteOpt =
                reportesService.generarReporteMensualPorPropiedad(propiedadId, anio, mes);

        if (reporteOpt.isEmpty()) {
            return Optional.of("No se encontro la propiedad solicitada");
        }

        ReportePropiedadMensualDTO reporte = reporteOpt.get();

        String asunto = "Reporte mensual propiedad #" + propiedadId + " - " + mes + "/" + anio;
        String html = construirCuerpoHtml(reporte);

        byte[] pdf;
        try {
            pdf = generarPdfDesdeHtml(html);
        } catch (IllegalStateException e) {
            return Optional.of("No fue posible generar el PDF del reporte");
        }

        try {
            MimeMessage mimeMessage = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            String from = Objects.requireNonNull(fromEmail, "MAIL_FROM no puede ser null");
            String htmlSeguro = Objects.requireNonNull(html, "HTML del reporte no puede ser null");
            byte[] pdfSeguro = Objects.requireNonNull(pdf, "PDF del reporte no puede ser null");

            helper.setFrom(from);
            helper.setTo(correos);
            helper.setSubject(asunto);
            helper.setText(htmlSeguro, true);
            helper.addAttachment(
                    "reporte-propiedad-" + propiedadId + "-" + anio + "-" + mes + ".pdf",
                new ByteArrayResource(pdfSeguro),
                    "application/pdf");

            sender.send(mimeMessage);
        } catch (MessagingException e) {
            return Optional.of("No fue posible enviar el correo: " + e.getMessage());
        }

        return Optional.empty();
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

    private String construirCuerpoHtml(ReportePropiedadMensualDTO reporte) {
        StringBuilder movimientosRows = new StringBuilder();
        for (ReportePropiedadMensualDTO.MovimientoResumenDTO mov : reporte.getMovimientos()) {
            movimientosRows.append("<tr>")
                    .append("<td>").append(escaparHtml(mov.getFecha() == null ? "-" : mov.getFecha().toString())).append("</td>")
                    .append("<td>").append(escaparHtml(mov.getTipo() == null ? "-" : mov.getTipo().name())).append("</td>")
                    .append("<td>").append(escaparHtml(mov.getConcepto() == null ? "-" : mov.getConcepto())).append("</td>")
                    .append("<td style='text-align:right;'>").append(mov.getMonto()).append("</td>")
                    .append("</tr>");
        }

        StringBuilder eventosRows = new StringBuilder();
        for (ReportePropiedadMensualDTO.EventoResumenDTO evt : reporte.getEventos()) {
            eventosRows.append("<tr>")
                    .append("<td>").append(escaparHtml(evt.getFecha() == null ? "-" : evt.getFecha().toString())).append("</td>")
                    .append("<td>").append(escaparHtml(evt.getTipo() == null ? "-" : evt.getTipo())).append("</td>")
                    .append("<td>").append(escaparHtml(evt.getDescripcion() == null ? "-" : evt.getDescripcion())).append("</td>")
                    .append("</tr>");
        }

        if (movimientosRows.length() == 0) {
            movimientosRows.append("<tr><td colspan='4'>Sin movimientos en el periodo</td></tr>");
        }

        if (eventosRows.length() == 0) {
            eventosRows.append("<tr><td colspan='3'>Sin eventos en el periodo</td></tr>");
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
                "<h1>Reporte Mensual de Propiedad</h1>" +
                "<div class='meta'>" +
                escaparHtml(reporte.getDireccion()) + ", " + escaparHtml(reporte.getComuna()) + " - " +
                escaparHtml(reporte.getCiudad()) + " | Periodo " + reporte.getMes() + "/" + reporte.getAnio() +
                "</div>" +
                "<div class='cards'>" +
                "<div class='card'><div class='k'>Ingresos</div><div class='v'>" + reporte.getTotalIngresos() + "</div></div>" +
                "<div class='card'><div class='k'>Egresos</div><div class='v'>" + reporte.getTotalEgresos() + "</div></div>" +
                "<div class='card'><div class='k'>Balance</div><div class='v'>" + reporte.getBalance() + "</div></div>" +
                "<div class='card'><div class='k'>Ocupacion</div><div class='v'>" + reporte.getPorcentajeOcupacion() + "%</div>" +
                "<div class='k'>" + reporte.getDiasOcupados() + "/" + reporte.getDiasTotalesMes() + " dias</div></div>" +
                "</div>" +
                "<h2>Movimientos</h2>" +
                "<table><thead><tr><th>Fecha</th><th>Tipo</th><th>Concepto</th><th>Monto</th></tr></thead><tbody>" +
                movimientosRows + "</tbody></table>" +
                "<h2>Eventos</h2>" +
                "<table><thead><tr><th>Fecha</th><th>Tipo</th><th>Descripcion</th></tr></thead><tbody>" +
                eventosRows + "</tbody></table>" +
                "</body></html>";
    }

    private String escaparHtml(@Nullable String valor) {
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
