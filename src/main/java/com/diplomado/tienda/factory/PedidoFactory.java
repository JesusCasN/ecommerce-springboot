package com.diplomado.tienda.factory;

import com.diplomado.tienda.model.*;

import java.util.List;

public interface PedidoFactory {

    Pedido crearPedido(Usuario usuario, FormasDePago formaPago,
                       Direccion direccion, double total, List<Carrito> productosEnCarrito);

    DetallePedido crearDetallePedido(Carrito item, Pedido pedido);
}
