package com.diplomado.tienda.factory;

import com.diplomado.tienda.model.Carrito;
import com.diplomado.tienda.model.Producto;
import com.diplomado.tienda.model.Usuario;

public interface CarritoFactory {

    Carrito crearCarrito(Usuario usuario, Producto producto, int cantidadSolicitada);
}
