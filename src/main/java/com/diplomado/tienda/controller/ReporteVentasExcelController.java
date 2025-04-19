package com.diplomado.tienda.controller;

import com.diplomado.tienda.dto.ReporteProducto;
import com.diplomado.tienda.reports.ReporteVentasExcelGenerator;
import com.diplomado.tienda.service.impl.PedidoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReporteVentasExcelController {

    private final PedidoServiceImpl pedidoServiceImpl;

    @GetMapping("/admin/reporte-ventas/excel")
    public ResponseEntity<byte[]> exportarReporteProductosAExcel(
            @RequestParam("fechaInicio") Optional<LocalDate> fechaInicio,
            @RequestParam("fechaFin") Optional<LocalDate> fechaFin) throws IOException {

        Optional<LocalDateTime> startDateTime = fechaInicio.map(LocalDate::atStartOfDay);
        Optional<LocalDateTime> endDateTime = fechaFin.map(date -> date.atTime(23, 59, 59));

        List<ReporteProducto> reportes = pedidoServiceImpl.generarReportePorProducto(
                startDateTime.map(Timestamp::valueOf),
                endDateTime.map(Timestamp::valueOf)
        );

        byte[] archivoExcel = ReporteVentasExcelGenerator.generarExcel(reportes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "reporte_ventas.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(archivoExcel);
    }
}
