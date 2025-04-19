package com.diplomado.tienda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO implements Serializable {

    private Long id_producto;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imagen;

}
