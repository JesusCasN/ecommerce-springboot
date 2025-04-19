package com.diplomado.tienda.factory.impl;

import com.diplomado.tienda.factory.PedidoFactory;
import com.diplomado.tienda.model.*;
import com.diplomado.tienda.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoFactoryImpl implements PedidoFactory {

    private final ProductoRepository productoRepository;

    @Override
    public Pedido crearPedido(Usuario usuario, FormasDePago formaPago, Direccion direccion, double total, List<Carrito> productosEnCarrito) {
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
        pedido.setFormaPago(formaPago);
        pedido.setDireccionEnvio(direccion);
        pedido.setTotal(total);

        List<DetallePedido> detalles = productosEnCarrito.stream()
                .map(item -> crearDetallePedido(item, pedido))
                .collect(Collectors.toList());

        pedido.setDetalles(detalles);
        return pedido;
    }

    @Override
    public DetallePedido crearDetallePedido(Carrito item, Pedido pedido) {
        Producto producto = item.getProducto();
        if (producto.getStock() < item.getCantidad()) {
            throw new IllegalArgumentException("No hay suficiente stock para el producto: " + producto.getNombre());
        }

        producto.setStock(producto.getStock() - item.getCantidad());
        productoRepository.save(producto);

        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setCantidad(item.getCantidad());
        detalle.setPrecio(item.getProducto().getPrecio().doubleValue());
        return detalle;
    }
}
