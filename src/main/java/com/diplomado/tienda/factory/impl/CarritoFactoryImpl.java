package com.diplomado.tienda.factory.impl;

import com.diplomado.tienda.model.Carrito;
import com.diplomado.tienda.model.Producto;
import com.diplomado.tienda.model.Usuario;
import com.diplomado.tienda.factory.CarritoFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CarritoFactoryImpl implements CarritoFactory {

    @Override
    public Carrito crearCarrito(Usuario usuario, Producto producto, int cantidadSolicitada) {
        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setProducto(producto);
        carrito.setCantidad(cantidadSolicitada);
        carrito.setFechaAgregado(new Date());
        return carrito;
    }

}
