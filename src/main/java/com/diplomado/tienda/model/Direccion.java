package com.diplomado.tienda.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "direccion")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDireccion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDireccion tipo;  // "Casa", "Trabajo", "Facturación"

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String codigoPostal;

    @Column(nullable = false)
    private String pais;

    @Enumerated(EnumType.STRING)
    @Column(name= "condicion", nullable = false)
    private EstadoDireccion condicion = EstadoDireccion.ACTIVO; //ACTIVO, ELIMINADO, INACTIVO
}
