package com.diplomado.tienda.reports;

import com.diplomado.tienda.dto.ReporteProducto;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class ReporteVentasPDFGenerator {

    private static final Logger logger = LoggerFactory.getLogger(ReporteVentasPDFGenerator.class);

    public static ByteArrayInputStream generarReporte(List<ReporteProducto> reportes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(out);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {

            // Título
            document.add(new Paragraph("Reporte de Ventas por Producto")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold());
            document.add(new Paragraph(" "));

            // Si la lista está vacía, agrega aviso y retorna
            if (reportes.isEmpty()) {
                document.add(new Paragraph("No hay datos disponibles para este reporte.")
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(12));
            } else {
                // Tabla
                Table table = new Table(new float[]{200f, 100f, 100f, 100f, 100f});
                table.setWidth(UnitValue.createPercentValue(100));

                table.addHeaderCell(new Cell().add(new Paragraph("Producto")).setTextAlignment(TextAlignment.CENTER).setBold());
                table.addHeaderCell(new Cell().add(new Paragraph("Fecha de Venta")).setTextAlignment(TextAlignment.CENTER).setBold());
                table.addHeaderCell(new Cell().add(new Paragraph("Cantidad Vendida")).setTextAlignment(TextAlignment.CENTER).setBold());
                table.addHeaderCell(new Cell().add(new Paragraph("Precio Unitario")).setTextAlignment(TextAlignment.CENTER).setBold());
                table.addHeaderCell(new Cell().add(new Paragraph("Ingreso Total")).setTextAlignment(TextAlignment.CENTER).setBold());

                double totalIngreso = 0.0;

                for (ReporteProducto r : reportes) {
                    table.addCell(new Paragraph(r.getProducto()).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Paragraph(r.getFechaVenta().toString()).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Paragraph(String.valueOf(r.getCantidadTotal())).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Paragraph(String.valueOf(r.getPrecioUnitario())).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Paragraph(String.valueOf(r.getIngresoTotal())).setTextAlignment(TextAlignment.CENTER));
                    totalIngreso += r.getIngresoTotal();
                }

                table.addCell(new Cell(1, 4).add(new Paragraph("Total Ingresos")).setTextAlignment(TextAlignment.RIGHT).setBold());
                table.addCell(new Paragraph(String.valueOf(totalIngreso)).setTextAlignment(TextAlignment.CENTER));

                document.add(table);
            }

        } catch (Exception e) {
            logger.error("Error al generar el reporte de ventas PDF", e);
            return new ByteArrayInputStream(new byte[0]);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

}
