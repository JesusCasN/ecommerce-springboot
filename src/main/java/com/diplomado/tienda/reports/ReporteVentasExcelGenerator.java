package com.diplomado.tienda.reports;

import com.diplomado.tienda.dto.ReporteProducto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ReporteVentasExcelGenerator {

    public static byte[] generarExcel(List<ReporteProducto> reportes) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("ReporteVentas");

            // Cabecera
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Producto");
            header.createCell(1).setCellValue("Fecha de Venta");
            header.createCell(2).setCellValue("Cantidad Vendida");
            header.createCell(3).setCellValue("Precio Unitario");
            header.createCell(4).setCellValue("Ingreso Total");

            // Datos
            int rowIdx = 1;
            for (ReporteProducto r : reportes) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(r.getProducto());
                row.createCell(1).setCellValue(r.getFechaVenta().toString());
                row.createCell(2).setCellValue(r.getCantidadTotal());
                row.createCell(3).setCellValue(r.getPrecioUnitario());
                row.createCell(4).setCellValue(r.getIngresoTotal());
            }

            // Autosize
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            return baos.toByteArray();
        }
    }
}
