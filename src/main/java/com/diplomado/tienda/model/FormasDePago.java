package com.diplomado.tienda.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "formas_de_pago")
public class FormasDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFormaPago;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String descripcion;

}
