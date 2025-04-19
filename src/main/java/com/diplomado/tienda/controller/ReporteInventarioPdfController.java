package com.diplomado.tienda.controller;

import com.diplomado.tienda.service.ProductoService;
import com.diplomado.tienda.reports.ReporteInventarioPDFGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.ByteArrayInputStream;

// Generar el reporte de inventario en PDF

@Controller
@RequiredArgsConstructor
public class ReporteInventarioPdfController {

    private final ProductoService productoService;


    @GetMapping("/admin/reporte-inventario/pdf")
    public ResponseEntity<InputStreamResource> generarReporteInventarioPDF() {
        ByteArrayInputStream bis = ReporteInventarioPDFGenerator.generarReporte(productoService.listarProductos());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=reporte_inventario.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}
