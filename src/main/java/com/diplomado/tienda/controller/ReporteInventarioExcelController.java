package com.diplomado.tienda.controller;

import com.diplomado.tienda.model.Producto;
import com.diplomado.tienda.reports.ReporteInventarioExcelGenerator;
import com.diplomado.tienda.service.impl.ProductoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReporteInventarioExcelController {

    private final ProductoServiceImpl productoServiceImpl;

    @GetMapping("/admin/reporte-productos/excel")
    public ResponseEntity<byte[]> exportarProductosAExcel(
            @RequestParam("fechaInicio") Optional<LocalDate> fechaInicio,
            @RequestParam("fechaFin") Optional<LocalDate> fechaFin) throws IOException {

        // Fechas por defecto si no se proporcionan
        LocalDate startDate = fechaInicio.orElse(LocalDate.of(1970, 1, 1));
        LocalDate endDate = fechaFin.orElse(LocalDate.now());

        // Obtener productos filtrados por fecha
        List<Producto> productos = productoServiceImpl.filtrarProductosPorFecha(startDate, endDate);

        // Generar el archivo Excel como byte[]
        byte[] archivoExcel = ReporteInventarioExcelGenerator.generarExcel(productos);

        // Configurar la respuesta HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "reporte_inventario.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(archivoExcel);
    }
}
