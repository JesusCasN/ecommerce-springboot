package com.diplomado.tienda.repository;

import com.diplomado.tienda.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    // Método personalizado para buscar roles por el nombre
    Optional<Rol> findByNombre(String nombre);
}
