package com.diplomado.tienda.reports;

import com.diplomado.tienda.model.Producto;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class ReporteInventarioPDFGenerator {

    // Crear un logger para esta clase
    private static final Logger logger = LoggerFactory.getLogger(ReporteInventarioPDFGenerator.class);

    public static ByteArrayInputStream generarReporte(List<Producto> productos) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Título
            Paragraph titulo = new Paragraph("Reporte de Inventario")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
            document.add(titulo);

            document.add(new Paragraph(" ")); // espacio

            // Tabla
            float[] columnWidths = {4, 4, 2, 2};
            Table table = new Table(columnWidths);
            table.setWidth(UnitValue.createPercentValue(100));

            // Encabezados
            table.addHeaderCell(new Cell().add(new Paragraph("Nombre")).setBold());
            table.addHeaderCell(new Cell().add(new Paragraph("Descripción")).setBold());
            table.addHeaderCell(new Cell().add(new Paragraph("Precio")).setBold());
            table.addHeaderCell(new Cell().add(new Paragraph("Stock")).setBold());

            // Datos
            for (Producto producto : productos) {
                table.addCell(new Cell().add(new Paragraph(producto.getNombre())));
                table.addCell(new Cell().add(new Paragraph(producto.getDescripcion())));
                table.addCell(new Cell().add(new Paragraph("$" + producto.getPrecio())));

                // Crear celda de stock con condición
                Paragraph stockParagraph = new Paragraph(String.valueOf(producto.getStock()))
                        .setTextAlignment(TextAlignment.CENTER);
                Cell stockCell = new Cell().add(stockParagraph);

                if (producto.getStock() < 20) {
                    stockCell.setBackgroundColor(ColorConstants.RED);
                    stockParagraph.setFontColor(ColorConstants.WHITE); // texto blanco si fondo rojo
                }

                table.addCell(stockCell);
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            logger.error("Error al generar el reporte de inventario en PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
