package com.diplomado.tienda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteProducto implements Serializable {

    private String producto;
    private LocalDate fechaVenta;
    private int cantidadTotal;
    private double precioUnitario;
    private double ingresoTotal;

}
