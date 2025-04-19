package com.diplomado.tienda.controller;

import com.diplomado.tienda.dto.ReporteProducto;
import com.diplomado.tienda.service.PedidoService;
import com.diplomado.tienda.reports.ReporteVentasPDFGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/reporte-productos")
public class ReporteVentasPdfController {

    private final PedidoService pedidoService;


    /**
     * Genera el reporte de ventas en PDF por producto en base a un rango de fechas.
     *
     * @param fechaInicio Fecha de inicio opcional para el filtro
     * @param fechaFin    Fecha de fin opcional para el filtro
     * @param model       Modelo para pasar los datos a la vista
     * @return La vista del reporte de ventas por producto
     */

    @GetMapping
    public String generarReportePorProducto(@RequestParam("fechaInicio") Optional<LocalDate> fechaInicio,
                                            @RequestParam("fechaFin") Optional<LocalDate> fechaFin,
                                            Model model) {

        // Llamar al método para obtener el reporte
        List<ReporteProducto> reportes = obtenerReporte(fechaInicio, fechaFin);

        // Pasar los reportes y las fechas al modelo para que puedan ser usados en la vista
        model.addAttribute("reportes", reportes);
        model.addAttribute("fechaInicio", fechaInicio.orElse(null));
        model.addAttribute("fechaFin", fechaFin.orElse(null));

        // Retornar la vista donde se muestra el reporte
        return "admin/reporteProductos";
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> exportarPDF(@RequestParam("fechaInicio") Optional<LocalDate> fechaInicio,
                                              @RequestParam("fechaFin") Optional<LocalDate> fechaFin) {

        List<ReporteProducto> reportes = obtenerReporte(fechaInicio, fechaFin);

        ByteArrayInputStream pdfStream = ReporteVentasPDFGenerator.generarReporte(reportes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "reporte_ventas.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfStream.readAllBytes());
    }


    // Método privado para convertir LocalDate a Timestamp y generar el reporte
    private List<ReporteProducto> obtenerReporte(Optional<LocalDate> fechaInicio, Optional<LocalDate> fechaFin) {
        Optional<Timestamp> inicio = fechaInicio.map(date -> Timestamp.valueOf(date.atStartOfDay()));
        Optional<Timestamp> fin = fechaFin.map(date -> Timestamp.valueOf(date.atTime(23, 59, 59)));

        // Llamar al servicio para generar el reporte filtrado por fecha
        return pedidoService.generarReportePorProducto(inicio, fin);
    }
}
