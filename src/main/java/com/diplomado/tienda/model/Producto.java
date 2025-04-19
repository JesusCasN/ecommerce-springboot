package com.diplomado.tienda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_producto;

    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @Column(nullable = false)
    @NotNull(message = "El precio no puede estar vacío")
    @DecimalMin(value = "0.01", inclusive = true, message = "El precio debe ser mayor que cero")
    private BigDecimal precio;

    @Column(nullable = false)
    @NotNull(message = "El stock no puede estar vacío")
    @Min(value = 0, message = "El stock debe ser mayor a 0")
    private int stock;

    @Column(nullable = false, updatable = false)
    @NotNull(message = "La fecha de creación no puede estar vacía")
    private LocalDateTime fecha_creacion;

    @Column()
    @NotBlank(message = "La imagen no puede estar vacía")
    private String imagen;

    // Relación ManyToOne con Categoria
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    // Asignar la fecha de creación automáticamente antes de persistir el objeto
    @PrePersist
    public void prePersist() {
        if (fecha_creacion == null) {
            this.fecha_creacion = LocalDateTime.now();
        }
    }

}