package com.diplomado.tienda.service.impl;

import com.diplomado.tienda.model.Carrito;
import com.diplomado.tienda.model.Producto;
import com.diplomado.tienda.model.Usuario;
import com.diplomado.tienda.repository.CarritoRepository;
import com.diplomado.tienda.factory.CarritoFactory;
import com.diplomado.tienda.repository.UsuarioRepository;
import com.diplomado.tienda.service.CarritoService;
import com.diplomado.tienda.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoFactory carritoFactory;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Carrito> obtenerProductosEnCarrito(String nombreUsuario) {

        log.info("\n📥 Obteniendo productos del carrito para el usuario: {} \n", nombreUsuario );

        Usuario usuario = usuarioRepository.findUsuarioByUsuario(nombreUsuario);
        List<Carrito> productosEnCarrito = carritoRepository.findByUsuario(usuario);

        productosEnCarrito.forEach(item -> {
            if (item.getProducto().getPrecio() == null) {
                log.warn("\n ⚠️ Producto sin precio (ID={}): asignando BigDecimal.ZERO \n", item.getProducto().getId_producto());
                item.getProducto().setPrecio(BigDecimal.ZERO);
            }
            if (item.getCantidad() == null) {
                log.warn("\n ⚠️ Producto sin cantidad (ID={}): asignando 0 \n", item.getProducto().getId_producto());
                item.setCantidad(0);
            }
        });

        return productosEnCarrito;
    }

    @Transactional
    @Override
    public void agregarProducto(Usuario usuario, Producto producto, int cantidadSolicitada) {

        log.info(""" 
                ➕ Agregando producto al carrito:
                   - Usuario: {}
                   - Producto: {}
                   - Cantidad solicitada: {}
                   - Stock disponible: {}
                """, usuario.getUsuario(), producto.getNombre(), cantidadSolicitada, producto.getStock());

        if (cantidadSolicitada > producto.getStock()) {
            log.error("\n ❌ Stock insuficiente para el producto '{}': solicitado={}, disponible={} \n",
                    producto.getNombre(), cantidadSolicitada, producto.getStock());
            throw new ProductoNoDisponibleException("No hay suficiente stock disponible para este producto.");
        }

        Carrito carritoExistente = carritoRepository.findByUsuarioAndProducto(usuario, producto);
        if (carritoExistente != null) {
            log.debug("\n 🔄 Producto ya existe en el carrito. Se actualizará la cantidad. \n");
            actualizarCantidadProductoExistente(carritoExistente, cantidadSolicitada, producto);
        } else {
            log.debug("🆕 Producto nuevo en el carrito. Creando entrada.");
            Carrito nuevoCarrito = carritoFactory.crearCarrito(usuario, producto, cantidadSolicitada);
            carritoRepository.save(nuevoCarrito);
        }
    }

    @Transactional
    @Override
    public void actualizarCantidadProductoExistente(Carrito carritoExistente, int cantidadSolicitada, Producto producto) {
        int nuevaCantidad = carritoExistente.getCantidad() + cantidadSolicitada;

        log.info("""
                🔁 Actualizando cantidad del producto existente en el carrito:
                   - Producto: {}
                   - Cantidad actual: {}
                   - Cantidad solicitada: {}
                   - Nueva cantidad: {}
                """, producto.getNombre(), carritoExistente.getCantidad(), cantidadSolicitada, nuevaCantidad);

        if (producto.getStock() < nuevaCantidad) {
            log.error("\n ❌ No hay suficiente stock para la nueva cantidad del producto '{}': requerido={}, disponible={} \n",
                    producto.getNombre(), nuevaCantidad, producto.getStock());
            throw new ProductoNoDisponibleException("No hay suficiente stock para aumentar la cantidad solicitada.");
        }

        carritoExistente.setCantidad(nuevaCantidad);
        carritoRepository.save(carritoExistente);
    }

    @Transactional
    @Override
    public void actualizarCantidadProducto(Usuario usuario, Producto producto, int nuevaCantidad) {

        log.info("""
                ✏️ Actualizando cantidad en el carrito:
                   - Usuario: {}
                   - Producto: {}
                   - Nueva cantidad: {}
                """, usuario.getUsuario(), producto.getNombre(), nuevaCantidad);


        Carrito carritoExistente = carritoRepository.findByUsuarioAndProducto(usuario, producto);
        if (carritoExistente == null) {
            log.warn("\n ⚠️ Producto '{}' no encontrado en el carrito del usuario '{}' \n", producto.getNombre(), usuario.getUsuario());
            throw new ProductoNoEncontradoEnCarritoException("El producto no existe en el carrito del usuario.");
        }

        if (nuevaCantidad > producto.getStock()) {
            log.error("\n ❌ No hay suficiente stock para actualizar el producto '{}': solicitado={}, disponible={} \n",
                    producto.getNombre(), nuevaCantidad, producto.getStock());
            throw new ProductoNoDisponibleException("No hay suficiente stock disponible para actualizar la cantidad.");
        }

        if (nuevaCantidad > 0) {
            carritoExistente.setCantidad(nuevaCantidad);
            carritoRepository.save(carritoExistente);
            log.info("\n ✅ Cantidad actualizada correctamente. \n");
        } else {
            carritoRepository.delete(carritoExistente);
            log.info("\n 🗑️ Producto eliminado del carrito ya que la cantidad es <= 0 \n");
        }
    }

    @Transactional
    @Override
    public void eliminarProducto(Usuario usuario, Producto producto) {
        log.info("\n ❌ Eliminando producto '{}' del carrito del usuario '{}' \n", producto.getNombre(), usuario.getUsuario());

        Carrito carritoExistente = carritoRepository.findByUsuarioAndProducto(usuario, producto);
        if (carritoExistente != null) {
            carritoRepository.delete(carritoExistente);
            log.info("\n ✅ Producto eliminado correctamente. \n");
        } else {
            log.warn("\n ⚠️ El producto no existe en el carrito del usuario '{}' \n", usuario.getUsuario());
            throw new ProductoNoEncontradoEnCarritoException("El producto no existe en el carrito del usuario.");
        }
    }

    @Transactional
    @Override
    public void vaciarCarrito(Usuario usuario) {
        log.info("\n 🧹 Vaciando el carrito del usuario '{}' \n", usuario.getUsuario());
        carritoRepository.deleteByUsuario(usuario);
        log.info("\n ✅ Carrito vaciado correctamente. \n");
    }
}
