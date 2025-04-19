package com.diplomado.tienda.reports;

import com.diplomado.tienda.model.Pedido;
import com.diplomado.tienda.model.DetallePedido;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
public class ComprobanteDePagoPDF {

    public byte[] generarComprobantePago(Pedido pedido) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Formato de fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss");
        String formattedDate = pedido.getFechaAsLocalDateTime().format(formatter);

        // Encabezado
        document.add(new Paragraph("Comprobante de Pago").setBold().setFontSize(18));
        document.add(new Paragraph("Fecha: " + formattedDate));
        document.add(new Paragraph("Pedido No: " + pedido.getId_pedido()));
        document.add(new Paragraph("Cliente: " + pedido.getUsuario().getUsuario()));
        document.add(new Paragraph("Total a Pagar: $" + pedido.getTotal() + " MXN"));
        document.add(new Paragraph(""));

        // Dirección de envío
        document.add(new Paragraph("Dirección de Envío:"));
        document.add(new Paragraph("Calle: " + pedido.getDireccionEnvio().getCalle()));
        document.add(new Paragraph("Ciudad: " + pedido.getDireccionEnvio().getCiudad()));
        document.add(new Paragraph("Estado: " + pedido.getDireccionEnvio().getEstado()));
        document.add(new Paragraph("Código Postal: " + pedido.getDireccionEnvio().getCodigoPostal()));
        document.add(new Paragraph(""));

        // Información de pago
        document.add(new Paragraph("Datos para el depósito o transferencia:"));
        document.add(new Paragraph("Banco: BBVA"));
        document.add(new Paragraph("Titular: Jose de Jesus Castillo Nolasco"));
        document.add(new Paragraph("Número de cuenta: 4152 3137 8092 3432"));
        document.add(new Paragraph("CLABE: 012345678901234567"));
        document.add(new Paragraph("No. Pedido " + pedido.getId_pedido()));
        document.add(new Paragraph(""));

        // Tabla con los productos comprados
        // Define anchos de columnas
        float[] columnWidths = {3, 1, 2, 2};
        Table table = new Table(columnWidths);

// Encabezados
        table.addHeaderCell(new Cell().add(new Paragraph("Producto")));
        table.addHeaderCell(new Cell().add(new Paragraph("Cantidad")));
        table.addHeaderCell(new Cell().add(new Paragraph("Precio Unitario")));
        table.addHeaderCell(new Cell().add(new Paragraph("Total")));

        BigDecimal totalGeneral = BigDecimal.ZERO;

        for (DetallePedido detalle : pedido.getDetalles()) {
            String nombre = detalle.getProducto().getNombre();
            int cantidad = detalle.getCantidad();
            BigDecimal precioUnitario = detalle.getProducto().getPrecio();

            // Cálculo total por producto
            BigDecimal totalPorProducto = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

            // Sumar al total general
            totalGeneral = totalGeneral.add(totalPorProducto);

            // Agregar fila
            table.addCell(new Cell().add(new Paragraph(nombre)));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(cantidad))));
            table.addCell(new Cell().add(new Paragraph("$" + precioUnitario)));
            table.addCell(new Cell().add(new Paragraph("$" + totalPorProducto)));
        }

        // Fila final: Total general
        table.addCell(new Cell(1, 3).add(new Paragraph("Total General")).setBold());
        table.addCell(new Cell().add(new Paragraph("$" + totalGeneral)).setBold());

        document.add(table);
        document.add(new Paragraph("\nGracias por su compra."));
        document.close();

        return outputStream.toByteArray();
    }
}
