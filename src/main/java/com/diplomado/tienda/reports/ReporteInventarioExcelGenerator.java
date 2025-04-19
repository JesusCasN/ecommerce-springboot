package com.diplomado.tienda.reports;

import com.diplomado.tienda.model.Producto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ReporteInventarioExcelGenerator {

    public static byte[] generarExcel(List<Producto> productos) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Productos");

            // Crear estilo para stock bajo (color rojo)
            CellStyle lowStockStyle = workbook.createCellStyle();
            Font redFont = workbook.createFont();
            redFont.setColor(IndexedColors.RED.getIndex());
            lowStockStyle.setFont(redFont);

            // Cabecera
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Nombre");
            header.createCell(2).setCellValue("Descripción");
            header.createCell(3).setCellValue("Precio");
            header.createCell(4).setCellValue("Stock");

            // Datos
            int rowIdx = 1;
            for (Producto p : productos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(p.getId_producto());
                row.createCell(1).setCellValue(p.getNombre());
                row.createCell(2).setCellValue(p.getDescripcion());
                row.createCell(3).setCellValue(p.getPrecio().doubleValue());

                Cell stockCell = row.createCell(4);
                stockCell.setCellValue(p.getStock());

                // Si el stock es bajo, aplicar estilo rojo
                if (p.getStock() < 20) {
                    stockCell.setCellStyle(lowStockStyle);
                }
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
