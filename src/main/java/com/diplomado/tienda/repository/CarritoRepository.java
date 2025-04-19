package com.diplomado.tienda.repository;

import com.diplomado.tienda.model.Carrito;
import com.diplomado.tienda.model.Usuario;
import com.diplomado.tienda.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    // Método para obtener todos los productos en el carrito de un usuario específico
    List<Carrito> findByUsuario(Usuario usuario);

    // Método para buscar un producto específico en el carrito de un usuario
    Carrito findByUsuarioAndProducto(Usuario usuario, Producto producto);

    // Método para eliminar todos los productos de un carrito de un usuario
    void deleteByUsuario(Usuario usuario);
}
