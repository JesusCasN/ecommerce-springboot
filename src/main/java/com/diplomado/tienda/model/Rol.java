package com.diplomado.tienda.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_rol;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column()
    private String descripcion;

    @OneToMany(mappedBy = "rol")
    private Set<Usuario> usuarios;

}
