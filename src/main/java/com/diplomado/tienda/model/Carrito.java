package com.diplomado.tienda.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")  // Asegura que coincida con la columna de la BD
    private Long idCarrito;  // Cambio a camelCase

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_agregado", nullable = false)
    private Date fechaAgregado;


    // Método para obtener el total del precio del producto en el carrito
    public BigDecimal getTotal() {
        if (producto != null && producto.getPrecio() != null && cantidad > 0) {
            return producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
        }
        return BigDecimal.ZERO;
    }
}
