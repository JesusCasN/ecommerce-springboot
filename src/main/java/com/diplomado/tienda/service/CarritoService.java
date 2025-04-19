package com.diplomado.tienda.service;

import com.diplomado.tienda.model.Carrito;
import com.diplomado.tienda.model.Producto;
import com.diplomado.tienda.model.Usuario;

import java.util.List;

public interface CarritoService {

    // Obtener los productos en el carrito de compras de un usuario
    List<Carrito> obtenerProductosEnCarrito(String nombreUsuario);

    // Agregar un producto al carrito de compras
    void agregarProducto(Usuario usuario, Producto producto, int cantidadSolicitada);

    // Actualizar la cantidad de un producto en el carrito
    void actualizarCantidadProducto(Usuario usuario, Producto producto, int nuevaCantidad);

    // Eliminar un producto del carrito
    void eliminarProducto(Usuario usuario, Producto producto);

    // Vaciar el carrito
    void vaciarCarrito(Usuario usuario);

    void actualizarCantidadProductoExistente(Carrito carritoExistente, int cantidadSolicitada, Producto producto);

}
