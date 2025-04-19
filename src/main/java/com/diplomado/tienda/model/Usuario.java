package com.diplomado.tienda.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;  // Cambiar a camelCase

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Column(nullable = false, unique = true)
    private String usuario;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String contraseña;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;  // Usando LocalDateTime en lugar de Timestamp

    @OneToMany(mappedBy = "usuario")
    private List<Direccion> direcciones;

}
